package com.aupnmt.api;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aupnmt.configuraion.JwtTokenUtil;
import com.aupnmt.dto.AccessToken;
import com.aupnmt.dto.Response;
import com.aupnmt.service.CommonService;
import com.aupnmt.service.OtpService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Login", description = "Login APIs")
public class CommonController {

	@Autowired
	CommonService commonService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@Autowired
	OtpService otpService;
	
	@Autowired
	CacheManager cacheManager;

	@GetMapping(value = "/login")
	public Response login(@RequestParam String phoneNumber) {
		Response response = new Response();
		try {
			AccessToken accessToken = commonService.userIdentification(phoneNumber);
			if (accessToken.getRole() != null) {
				cacheManager.getCache("default").evictIfPresent(phoneNumber);
				Integer otp = otpService.generateOTP(phoneNumber, false);
				authenticate(phoneNumber, otp.toString());
				final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(phoneNumber);
				final String token = jwtTokenUtil.generateToken(userDetails);
				accessToken.setAccessToken(token);
				accessToken.setApplicationSubmittedStatus(true);
				response.setData(accessToken);
				response.setMessage("Logged in successfully for the Phone Number: " + phoneNumber);
				response.setStatus("Success");
			} else {
				response.setMessage("User doesn't exists. Logging in is failed to the Phone Number: " + phoneNumber);
				response.setStatus("Failure");
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			response.setMessage("Logging in is failed to the Phone Number: " + phoneNumber);
			response.setStatus("Failure");
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Logging in is failed to the Phone Number: " + phoneNumber);
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

}