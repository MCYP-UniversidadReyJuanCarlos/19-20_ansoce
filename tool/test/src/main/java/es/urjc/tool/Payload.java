package es.urjc.tool;

public class Payload {

	private String urlLogin;

	private String urlOk;

	public Payload() {
	}

	public Payload(String urlLogin, String urlOk) {
		this.urlLogin = urlLogin;
		this.urlOk = urlOk;
	}

	public String getUrlLogin() {
		return this.urlLogin;
	}

	public void setUrlLogin(String urlLogin) {
		this.urlLogin = urlLogin;
	}

	public String getUrlOk() {
		return this.urlOk;
	}

	public void setUrlOk(String urlOk) {
		this.urlOk = urlOk;
	}

}
