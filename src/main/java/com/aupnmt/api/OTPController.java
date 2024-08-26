package com.aupnmt.api;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aupnmt.configuraion.JwtTokenUtil;
import com.aupnmt.dto.AccessToken;
import com.aupnmt.dto.Otp;
import com.aupnmt.dto.Response;
import com.aupnmt.exception.spammingException;
import com.aupnmt.service.CommonService;
import com.aupnmt.service.OtpService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "OTP", description = "OTP APIs")
public class OTPController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@Autowired
	OtpService otpService;

	@Autowired
	CommonService commonService;

	@Autowired
	CacheManager cacheManager;

	@PostMapping("/otpGenerate")
	public Response otpGenerate(@RequestBody Otp otpp) throws spammingException {
		Response response = new Response();
		try {
			cacheManager.getCache("default").evictIfPresent(otpp.getPhoneNumber());
			Integer otp = otpService.generateOTP(otpp.getPhoneNumber(), true);
			System.out.println(otp);

			if (otp != 0) {
				response.setMessage(
						"OTP generated and sent successfully to the Phone Number: " + otpp.getPhoneNumber());
				response.setStatus("Success");
			} else {
				response.setMessage("OTP generation is failed to the Phone Number: " + otpp.getPhoneNumber());
				response.setStatus("Failure");
			}
			return response;
		} catch (Exception e) {
			throw new spammingException(e.getMessage());
		}
	}

	@PostMapping("/otpValidate")
	public Response otpValidate(@RequestBody Otp Otp) throws Exception {
		Response response = new Response();
		try {
			authenticate(Otp.getPhoneNumber(), Otp.getOtp().toString());
			final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(Otp.getPhoneNumber());
			final String token = jwtTokenUtil.generateToken(userDetails);
			AccessToken accessToken = commonService.userIdentification(Otp.getPhoneNumber());
			if (accessToken.getRole() != null) {
				accessToken.setApplicationSubmittedStatus(true);
			} else {
				accessToken.setApplicationSubmittedStatus(false);
			}
			accessToken.setAccessToken(token);
			response.setData(accessToken);
			response.setMessage("OTP verified successfully to the Phone Number: " + Otp.getPhoneNumber());
			response.setStatus("Success");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			response.setMessage("OTP verification is failed to the Phone Number: " + Otp.getPhoneNumber());
			response.setStatus("Failure");
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("OTP verification is failed to the Phone Number: " + Otp.getPhoneNumber());
			response.setStatus("Failure");
		}
		return response;
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	@ExceptionHandler(spammingException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String itemNotExistExceptionHandler(spammingException ex) {
		return ex.getMessage();
	}

}
