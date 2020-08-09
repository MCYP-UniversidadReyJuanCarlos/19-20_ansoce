package es.urjc.oauth2.ccgt.client.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Controller
public class AuthorizationController {

	@Value("${messages.base-uri}")
	private String messagesBaseUri;

	@Autowired
	private WebClient webClient;

	@GetMapping("/message")
	public String message(Model model) {
		try {
			String messages = retrieveMessages("custom-client", "/mensaje");
			model.addAttribute("messages", messages);
			return "message";
		}
		catch (WebClientResponseException ex) {
			return getError(model, ex);
		}
	}

	@GetMapping("/messageadmin")
	public String messageadmin(Model model) {
		try {
			String messages = retrieveMessages("custom-client", "/administrador");
			model.addAttribute("messages", messages);
			return "message";
		}
		catch (WebClientResponseException ex) {
			return getError(model, ex);
		}

	}

	private String getError(Model model, WebClientResponseException ex) {
		if (ex.getStatusCode().value() != 403) {
			return "accessDenied";
		}
		else {
			model.addAttribute("errorCode", ex.getStatusCode());
			model.addAttribute("error", ex.getLocalizedMessage());
			return "error";
		}
	}

	private String retrieveMessages(String clientRegistrationId, String resource) {
		return this.webClient.get().uri(this.messagesBaseUri + resource)
				.attributes(
						ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId(clientRegistrationId))
				.retrieve().bodyToMono(String.class).block();

	}

}