package com.project.resp.vo;

public class ResponderVO {
	
	private String endpoint;
	
	private String responseName;
	
	private int status;
	
	private String contentType;
	
	private String responseBody;
	
	private String responseFileName;

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getResponseName() {
		return responseName;
	}

	public void setResponseName(String responseName) {
		this.responseName = responseName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getResponseFileName() {
		return responseFileName;
	}

	public void setResponseFileName(String responseFileName) {
		this.responseFileName = responseFileName;
	}

		

}
