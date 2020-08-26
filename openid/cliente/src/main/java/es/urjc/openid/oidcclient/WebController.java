package es.urjc.openid.oidcclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Controller
public class WebController {

	@Value("${messages.base-uri}")
	private String messagesBaseUri;

	private final WebClient webClient;

	public WebController(WebClient webClient) {
		this.webClient = webClient;
	}

	@GetMapping("/")
	public String index(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
			@AuthenticationPrincipal OidcUser oauth2User) {
		model.addAttribute("userName", (oauth2User != null) ? oauth2User.getName() : "");
		model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
		return "index";
	}

	@GetMapping("/mensaje")
	public String message(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
		try {

			String messages = retrieveMessages(authorizedClient.getClientRegistration().getRegistrationId(),
					"/mensaje");
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
				.attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction
						.clientRegistrationId((clientRegistrationId.contains("okta")) ? "okta" : clientRegistrationId))
				.retrieve()
				.onStatus((s) -> s.equals(HttpStatus.UNAUTHORIZED),
						(cr) -> Mono.just(new BadCredentialsException("Not authenticated")))
				.onStatus(HttpStatus::is4xxClientError,
						(cr) -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
				.onStatus(HttpStatus::is5xxServerError,
						(cr) -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
				.bodyToMono(String.class).block();

	}

}