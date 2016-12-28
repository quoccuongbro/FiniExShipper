package model;

public class AccessToken {
	private String accessToken;
	private String userId;
	public AccessToken(String accessToken, String userId) {
		super();
		this.accessToken = accessToken;
		this.userId = userId;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
}
