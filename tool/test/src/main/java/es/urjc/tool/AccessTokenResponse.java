package es.urjc.tool;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class AccessTokenResponse {

	@JsonProperty("access_token")
	private String token;

	@JsonProperty("expires_in")
	private long expiresIn;

	@JsonProperty("refresh_expires_in")
	private long refreshExpiresIn;

	@JsonProperty("refresh_token")
	private String refreshToken;

	@JsonProperty("token_type")
	private String tokenType;

	@JsonProperty("id_token")
	private String idToken;

	@JsonProperty("not-before-policy")
	private int notBeforePolicy;

	@JsonProperty("session_state")
	private String sessionState;

	private final Map<String, Object> otherClaims = new HashMap<>();

	// OIDC Financial API Read Only Profile : scope MUST be returned in the response from
	// Token Endpoint
	@JsonProperty("scope")
	private String scope;

	public String getScope() {
		return this.scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getExpiresIn() {
		return this.expiresIn;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public long getRefreshExpiresIn() {
		return this.refreshExpiresIn;
	}

	public void setRefreshExpiresIn(long refreshExpiresIn) {
		this.refreshExpiresIn = refreshExpiresIn;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getTokenType() {
		return this.tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getIdToken() {
		return this.idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	public int getNotBeforePolicy() {
		return this.notBeforePolicy;
	}

	public void setNotBeforePolicy(int notBeforePolicy) {
		this.notBeforePolicy = notBeforePolicy;
	}

	public String getSessionState() {
		return this.sessionState;
	}

	public void setSessionState(String sessionState) {
		this.sessionState = sessionState;
	}

	@JsonAnyGetter
	public Map<String, Object> getOtherClaims() {
		return this.otherClaims;
	}

	@JsonAnySetter
	public void setOtherClaims(String name, Object value) {
		this.otherClaims.put(name, value);
	}

}
