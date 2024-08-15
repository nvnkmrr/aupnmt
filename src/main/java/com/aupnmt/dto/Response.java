package com.aupnmt.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

	private String status;
	private String message;
	@JsonProperty(value = "data", required = true)
	private String data;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getData() {
		return data;
	}

	public void setData(Object data) throws JsonProcessingException {
		if(Objects.nonNull(data)) {
			this.data = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(data);
		}else {
			this.data = "";
		}
		
	}

}
