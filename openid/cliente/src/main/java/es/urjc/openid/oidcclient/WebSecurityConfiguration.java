package es.urjc.openid.oidcclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import java.net.URI;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	ClientRegistrationRepository clientRegistrationRepository;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/js/*", "/css/*", "/images/*", "/font-awesome/**").permitAll()
				.anyRequest().fullyAuthenticated().and().logout().logoutSuccessHandler(oidcLogoutSuccessHandler())
				.invalidateHttpSession(true).deleteCookies("JSESSIONID").and().exceptionHandling()
				.accessDeniedPage("/accessDenied").and().oauth2Client().and().oauth2Login().userInfoEndpoint();
	}

	OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
		OidcClientInitiatedLogoutSuccessHandler successHandler = new OidcClientInitiatedLogoutSuccessHandler(
				this.clientRegistrationRepository);
		successHandler.setPostLogoutRedirectUri(URI.create("http://localhost:9090"));
		return successHandler;
	}

}
