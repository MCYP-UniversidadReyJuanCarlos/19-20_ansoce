package es.urjc.tool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Component
public class VariablesConfig {

	@Value("${validation.tokenOk}")
	private boolean tokenOk;

	@Value("${validation.tokenExpired}")
	private boolean tokenExpired;

	@Value("${validation.tokenBadSigned}")
	private boolean tokenBadSigned;

	@Value("${validation.tokenBadClaims}")
	private boolean tokenBadClaims;


	public boolean isTokenOk() {
		return this.tokenOk;
	}

	public void setTokenOk(boolean tokenOk) {
		this.tokenOk = tokenOk;
	}

	public boolean isTokenExpired() {
		return this.tokenExpired;
	}

	public void setTokenExpired(boolean tokenExpired) {
		this.tokenExpired = tokenExpired;
	}

	public boolean isTokenBadSigned() {
		return this.tokenBadSigned;
	}

	public void setTokenBadSigned(boolean tokenBadSigned) {
		this.tokenBadSigned = tokenBadSigned;
	}

	public boolean isTokenBadClaims() {
		return this.tokenBadClaims;
	}

	public void setTokenBadClaims(boolean tokenBadClaims) {
		this.tokenBadClaims = tokenBadClaims;
	}

	public void resetTrue(){
		this.tokenOk=true;
		this.tokenExpired=true;
		this.tokenBadSigned=true;
		this.tokenBadClaims=true;
	}
}
