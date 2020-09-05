package es.urjc.tool.oidcclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.authentication.OidcIdTokenDecoderFactory;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoderFactory;

import java.net.URI;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	ClientRegistrationRepository clientRegistrationRepository;

	@Autowired
	private CustomAuthenticationProvider authProvider;

	@Autowired
	private JwtCustomValidator jwtCustomValidator;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(this.authProvider);
		this.authProvider.setJwtDecoderFactory(this.idTokenDecoderFactory());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authenticationProvider(this.authProvider).authorizeRequests()
				.antMatchers("/js/*", "/css/*", "/images/*", "/font-awesome/**").permitAll().anyRequest()
				.fullyAuthenticated().and().logout().logoutSuccessHandler(oidcLogoutSuccessHandler())
				.invalidateHttpSession(true).deleteCookies("JSESSIONID").and().exceptionHandling()
				.accessDeniedPage("/accessDenied").and().oauth2Client().and().oauth2Login();
	}

	OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
		OidcClientInitiatedLogoutSuccessHandler successHandler = new OidcClientInitiatedLogoutSuccessHandler(
				this.clientRegistrationRepository);
		successHandler.setPostLogoutRedirectUri(URI.create("http://localhost:9090"));
		return successHandler;
	}

	@Bean
	public JwtDecoderFactory<ClientRegistration> idTokenDecoderFactory() {
		OidcIdTokenDecoderFactory idTokenDecoderFactory = new OidcIdTokenDecoderFactory();

		idTokenDecoderFactory.setJwtValidatorFactory((clientRegistration) -> {
			this.jwtCustomValidator.setClientRegistration(clientRegistration);
			return this.jwtCustomValidator;
		});

		return idTokenDecoderFactory;
	}

}
