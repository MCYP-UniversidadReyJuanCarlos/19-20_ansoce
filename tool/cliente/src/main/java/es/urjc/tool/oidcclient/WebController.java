package es.urjc.tool.oidcclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.stream.Collectors;

@Controller
public class WebController {

	@Autowired
	private VariablesConfig variablesConfig;

	@GetMapping("/")
	public String index(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
			@AuthenticationPrincipal OidcUser oauth2User) {
		model.addAttribute("userName", (oauth2User != null) ? oauth2User.getName() : "");
		model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
		model.addAttribute("userAttributes", (oauth2User != null) ? oauth2User.getAttributes() : null);
		model.addAttribute("userScopes",
				(oauth2User != null)
						? oauth2User.getAuthorities().stream().filter((sc) -> sc instanceof SimpleGrantedAuthority)
								.map((sc) -> sc.toString()).collect(Collectors.toList())
						: null);
		model.addAttribute("userRoles",
				(oauth2User != null)
						? oauth2User.getAuthorities().stream().filter((sc) -> sc instanceof OidcUserAuthority)
								.map((sc) -> sc.toString()).collect(Collectors.toList())
						: null);
		return "index";
	}

	@PostMapping
	public void configValidations(@ModelAttribute VariablesConfig payload) {
		this.variablesConfig = payload;
	}

}