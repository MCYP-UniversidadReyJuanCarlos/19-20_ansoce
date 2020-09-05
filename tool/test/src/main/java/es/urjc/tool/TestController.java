package es.urjc.tool;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TestController {

	private WebDriver driver;

	private boolean bWorking = false;

	JavascriptExecutor js;

	private final VariablesConfig variablesConfig;

	public TestController(VariablesConfig variablesConfig) {
		this.variablesConfig = variablesConfig;
	}

	@PostMapping("/test")
	public String test(@ModelAttribute Payload payload, Model model) {
		List<String> errors = new ArrayList<>();
		String result = "badConfig";
		while (this.bWorking) {
			try {
				Thread.sleep(2000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {

			this.bWorking = true;
			// token ok
			this.variablesConfig.setTokenOk(true);
			this.variablesConfig.setTokenBadClaims(false);
			this.variablesConfig.setTokenExpired(false);
			this.variablesConfig.setTokenBadSigned(false);
			Boolean bTokenOk = runTest(payload.getUrlLogin(), payload.getUrlOk());

			// token firma mal
			this.variablesConfig.setTokenOk(false);
			this.variablesConfig.setTokenBadSigned(true);
			Boolean bBadsignal = runTest(payload.getUrlLogin(), payload.getUrlOk());

			// token timestamp caducado
			this.variablesConfig.setTokenBadSigned(false);
			this.variablesConfig.setTokenExpired(true);
			Boolean bExpired = runTest(payload.getUrlLogin(), payload.getUrlOk());

			// resto de claims
			this.variablesConfig.setTokenExpired(false);
			this.variablesConfig.setTokenBadClaims(true);
			Boolean bClaims = runTest(payload.getUrlLogin(), payload.getUrlOk());

			if (bTokenOk && !bBadsignal && !bExpired && !bClaims) {
				result = "goodConfig";
			}
			else if (!bTokenOk) {
				model.addAttribute("Error", "La aplicación no verifica correctamente un token valido.");
			}
			else {
				if (bBadsignal) {
					errors.add("La aplicación no esta comprobando la firma del token");
				}
				if (bExpired) {
					errors.add("La aplicación no esta comprobando la fecha de expiración del token");
				}
				if (bClaims) {
					errors.add("La aplicación no esta comprobando los claims correctamente");
				}
			}
			model.addAttribute("errors", errors);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			errors = new ArrayList<>();
			errors.add(
					"Se ha producido un error, es posible que el cliente no este inicializado, vuelva a intentarlo.");
		}
		finally {
			this.bWorking = false;
			this.variablesConfig.resetTrue();
		}
		return result;
	}

	private Boolean runTest(String loginUrl, String okUrl) {
		try {
			this.setUp();
		}
		catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
		Boolean result = this.login(loginUrl, okUrl);
		this.tearDown();
		return result;
	}

	public void setUp() throws MalformedURLException {
		String remoteUrl = "http://selenium:4444/wd/hub";
		ChromeOptions cp = new ChromeOptions();
		cp.addArguments("--incognito");
		this.driver = new RemoteWebDriver(new URL(remoteUrl), cp);
		this.js = (JavascriptExecutor) this.driver;
	}

	public void tearDown() {
		this.driver.quit();
	}

	public Boolean login(String loginUrl, String okUrl) {
		this.driver.get(loginUrl);
		this.driver.findElement(By.linkText("http://keycloak:8080/auth/realms/TFM")).click();
		this.driver.findElement(By.id("username")).sendKeys("arturo");
		this.driver.findElement(By.id("password")).sendKeys("secret");
		this.driver.findElement(By.id("kc-login")).click();
		try {
			this.driver.findElement(By.id("kc-login")).click();
		}
		catch (Exception ex) {
		}
		return okUrl.equals(this.driver.getCurrentUrl());
	}

}