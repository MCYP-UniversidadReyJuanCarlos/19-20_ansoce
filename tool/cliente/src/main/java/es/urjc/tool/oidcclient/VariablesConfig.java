package es.urjc.tool.oidcclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Component
public class VariablesConfig {

	@Value("${validation.VerifiTimestamp}")
	private boolean bVerifiTimestamp;

	@Value("${validation.VerifiOidc}")
	private boolean bVerifiOidc;

	@Value("${validation.VerifiSign}")
	private boolean bVerifiSign;

	public boolean isbVerifiTimestamp() {
		return this.bVerifiTimestamp;
	}

	public void setbVerifiTimestamp(boolean bVerifiTimestamp) {
		this.bVerifiTimestamp = bVerifiTimestamp;
	}

	public boolean isbVerifiOidc() {
		return this.bVerifiOidc;
	}

	public void setbVerifiOidc(boolean bVerifiOidc) {
		this.bVerifiOidc = bVerifiOidc;
	}

	public boolean isbVerifiSign() {
		return this.bVerifiSign;
	}

	public void setbVerifiSign(boolean bVerifiSign) {
		this.bVerifiSign = bVerifiSign;
	}

}
