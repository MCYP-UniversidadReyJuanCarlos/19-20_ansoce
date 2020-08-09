package es.urjc.oauth2.authserv.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.security.KeyPair;

@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	AuthenticationManager authenticationManager;

	KeyPair keyPair;

	boolean jwtEnabled;

	public AuthorizationServerConfiguration(AuthenticationConfiguration authenticationConfiguration, KeyPair keyPair,
			@Value("${security.oauth2.authorizationserver.jwt.enabled:true}") boolean jwtEnabled) throws Exception {

		this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
		this.keyPair = keyPair;
		this.jwtEnabled = jwtEnabled;
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// @formatter:off
		clients.inMemory()
				.withClient("user")
				.authorizedGrantTypes("client_credentials", "password", "authorization_code", "implicit")
				.secret("{noop}secret")
				.scopes("user")
				.accessTokenValiditySeconds(120)
				.redirectUris("http://127.0.0.1:9090/mensaje", "http://127.0.0.1:9090//authorized")
				.and()
				.withClient("admin")
				.authorizedGrantTypes("client_credentials", "password", "authorization_code", "implicit")
				.secret("{noop}secret")
				.scopes("admin")
				.accessTokenValiditySeconds(120)
				.redirectUris("http://127.0.0.1:9090/mensaje", "http://127.0.0.1:9090//authorized")
				.and()
				.withClient("noscopes")
				.authorizedGrantTypes("client_credentials", "password", "authorization_code", "implicit")
				.secret("{noop}secret")
				.scopes("none")
				.accessTokenValiditySeconds(120)
				.redirectUris("http://127.0.0.1:9090/mensaje", "http://127.0.0.1:9090//authorized");
		// @formatter:on
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints.authenticationManager(this.authenticationManager).tokenStore(tokenStore());

		if (this.jwtEnabled) {
			endpoints.accessTokenConverter(accessTokenConverter());
		}
	}

	@Bean
	public TokenStore tokenStore() {
		if (this.jwtEnabled) {
			return new JwtTokenStore(accessTokenConverter());
		}
		else {
			return new InMemoryTokenStore();
		}
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setKeyPair(this.keyPair);

		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		accessTokenConverter.setUserTokenConverter(new SubjectAttributeUserTokenConverter());
		converter.setAccessTokenConverter(accessTokenConverter);

		return converter;
	}

}
