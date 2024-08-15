package com.aupnmt.api;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aupnmt.configuraion.JwtTokenUtil;
import com.aupnmt.dto.AccessToken;
import com.aupnmt.dto.Response;
import com.aupnmt.service.OtpService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class OTPController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;

	@Autowired
	OtpService otpService;

	@PostMapping("/otpGenerate")
	public Response otpGenerate(@RequestParam String phoneNumber) {

		Integer otp = otpService.generateOTP(phoneNumber, true);
		System.out.println(otp);
		Response response = new Response();
		if (otp != 0) {
			response.setMessage("OTP generated and sent successfully to the Phone Number: " + phoneNumber);
			response.setStatus("Success");
		} else {
			response.setMessage("OTP generation is failed to the Phone Number: " + phoneNumber);
			response.setStatus("Failure");
		}
		return response;
	}

	@PostMapping("/otpValidate")
	public Response otpValidate(@RequestParam String phoneNumber, @RequestParam Integer otp) throws Exception {
		Response response = new Response();
		try {
			authenticate(phoneNumber, otp.toString());
			final UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(phoneNumber);
			final String token = jwtTokenUtil.generateToken(userDetails);
			AccessToken accessToken = new AccessToken();
			accessToken.setAccessToken(token);
			response.setData(accessToken);
			response.setMessage("OTP verified successfully to the Phone Number: " + phoneNumber);
			response.setStatus("Success");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			response.setMessage("OTP verification is failed to the Phone Number: " + phoneNumber);
			response.setStatus("Failure");
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("OTP verification is failed to the Phone Number: " + phoneNumber);
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
