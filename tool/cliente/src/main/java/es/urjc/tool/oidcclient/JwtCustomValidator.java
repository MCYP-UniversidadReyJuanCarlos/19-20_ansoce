package es.urjc.tool.oidcclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.authentication.OidcIdTokenValidator;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Predicate;

@Component
public class JwtCustomValidator<T extends AbstractOAuth2Token> implements OAuth2TokenValidator<T> {

	private Collection<OAuth2TokenValidator<T>> tokenValidators = new ArrayList<>();

	@Autowired
	private VariablesConfig variablesConfig;

	private ClientRegistration clientRegistration = null;

	public void setClientRegistration(ClientRegistration clientRegistration) {
		this.clientRegistration = clientRegistration;
	}

	@Override
	public OAuth2TokenValidatorResult validate(T jwt) {
		Collection<OAuth2Error> errors = new ArrayList();
		this.tokenValidators = new ArrayList<>();
		if (this.variablesConfig.isbVerifiTimestamp()) {
			this.tokenValidators.add((OAuth2TokenValidator<T>) new JwtTimestampValidator());
		}
		// La validacion de los claims incluye el tiempo de expiracion
		if (this.variablesConfig.isbVerifiOidc() && null != this.clientRegistration) {
			this.tokenValidators.add((OAuth2TokenValidator<T>) new OidcIdTokenValidator(this.clientRegistration));
		}
		Iterator var3 = this.tokenValidators.iterator();

		while (var3.hasNext()) {
			OAuth2TokenValidator<T> validator = (OAuth2TokenValidator) var3.next();
			if (validator instanceof OidcIdTokenValidator && !this.variablesConfig.isbVerifiTimestamp()) {
				Predicate<OAuth2Error> isExpError = (item) -> item.getDescription()
						.contains("The ID Token contains invalid claims: {exp=");
				Collection<OAuth2Error> errorsVal = validator.validate(jwt).getErrors();
				Collection<OAuth2Error> errorsToDelete = new ArrayList<>();
				errorsVal.stream().filter((item) -> isExpError.test(item)).forEach(errorsToDelete::add);
				errorsVal.removeAll(errorsToDelete);
				errors.addAll(errorsVal);
			}
			else {
				errors.addAll(validator.validate(jwt).getErrors());
			}

		}
		return OAuth2TokenValidatorResult.failure(errors);
	}

}