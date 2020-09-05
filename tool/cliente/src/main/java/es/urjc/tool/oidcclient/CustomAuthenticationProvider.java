package es.urjc.tool.oidcclient;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;

@Component
@SuppressWarnings("checkstyle:AvoidStarImport")
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private static final String INVALID_STATE_PARAMETER_ERROR_CODE = "invalid_state_parameter";

	private static final String INVALID_ID_TOKEN_ERROR_CODE = "invalid_id_token";


	private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient;

	private final OAuth2UserService<OidcUserRequest, OidcUser> userService;

	private Converter<Map<String, Object>, Map<String, Object>> claimSetConverter = MappedJwtClaimSetConverter
			.withDefaults(Collections.emptyMap());

	@Autowired
	private JwtDecoderFactory<ClientRegistration> jwtDecoderFactory;

	@Autowired
	private JwtCustomValidator jwtCustomValidator;

	@Autowired
	private VariablesConfig variablesConfig;

	private GrantedAuthoritiesMapper authoritiesMapper = ((authorities) -> authorities);

	public CustomAuthenticationProvider() {
		this.accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
		this.userService = new OidcUserService();

	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		OAuth2LoginAuthenticationToken authorizationCodeAuthentication = (OAuth2LoginAuthenticationToken) authentication;

		// Section 3.1.2.1 Authentication Request -
		// https://openid.net/specs/openid-connect-core-1_0.html#AuthRequest
		// scope
		// REQUIRED. OpenID Connect requests MUST contain the "openid" scope value.
		if (!authorizationCodeAuthentication.getAuthorizationExchange().getAuthorizationRequest().getScopes()
				.contains(OidcScopes.OPENID)) {
			// This is NOT an OpenID Connect Authentication Request so return null
			// and let OAuth2LoginAuthenticationProvider handle it instead
			return null;
		}

		OAuth2AuthorizationRequest authorizationRequest = authorizationCodeAuthentication.getAuthorizationExchange()
				.getAuthorizationRequest();
		OAuth2AuthorizationResponse authorizationResponse = authorizationCodeAuthentication.getAuthorizationExchange()
				.getAuthorizationResponse();

		if (authorizationResponse.statusError()) {
			throw new OAuth2AuthenticationException(authorizationResponse.getError(),
					authorizationResponse.getError().toString());
		}

		if (!authorizationResponse.getState().equals(authorizationRequest.getState())) {
			OAuth2Error oauth2Error = new OAuth2Error(INVALID_STATE_PARAMETER_ERROR_CODE);
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
		}

		OAuth2AccessTokenResponse accessTokenResponse;
		try {
			accessTokenResponse = this.accessTokenResponseClient.getTokenResponse(
					new OAuth2AuthorizationCodeGrantRequest(authorizationCodeAuthentication.getClientRegistration(),
							authorizationCodeAuthentication.getAuthorizationExchange()));
		}
		catch (OAuth2AuthorizationException ex) {
			OAuth2Error oauth2Error = ex.getError();
			throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
		}

		ClientRegistration clientRegistration = authorizationCodeAuthentication.getClientRegistration();

		Map<String, Object> additionalParameters = accessTokenResponse.getAdditionalParameters();
		if (!additionalParameters.containsKey(OidcParameterNames.ID_TOKEN)) {
			OAuth2Error invalidIdTokenError = new OAuth2Error(INVALID_ID_TOKEN_ERROR_CODE,
					"Missing (required) ID Token in Token Response for Client Registration: "
							+ clientRegistration.getRegistrationId(),
					null);
			throw new OAuth2AuthenticationException(invalidIdTokenError, invalidIdTokenError.toString());
		}
		OidcIdToken idToken = createOidcToken(clientRegistration, accessTokenResponse);

		OidcUser oidcUser = this.loadUser(new OidcUserRequest(clientRegistration, accessTokenResponse.getAccessToken(),
				idToken, additionalParameters));

		Collection<? extends GrantedAuthority> mappedAuthorities = this.authoritiesMapper
				.mapAuthorities(oidcUser.getAuthorities());

		OAuth2LoginAuthenticationToken authenticationResult = new OAuth2LoginAuthenticationToken(
				authorizationCodeAuthentication.getClientRegistration(),
				authorizationCodeAuthentication.getAuthorizationExchange(), oidcUser, mappedAuthorities,
				accessTokenResponse.getAccessToken(), accessTokenResponse.getRefreshToken());
		authenticationResult.setDetails(authorizationCodeAuthentication.getDetails());

		return authenticationResult;
	}

	public final void setJwtDecoderFactory(JwtDecoderFactory<ClientRegistration> jwtDecoderFactory) {
		Assert.notNull(jwtDecoderFactory, "jwtDecoderFactory cannot be null");
		this.jwtDecoderFactory = jwtDecoderFactory;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return OAuth2LoginAuthenticationToken.class.isAssignableFrom(authentication);
	}

	private OidcIdToken createOidcToken(ClientRegistration clientRegistration,
			OAuth2AccessTokenResponse accessTokenResponse) {

		Jwt jwt = null;

		try {
			if (this.variablesConfig.isbVerifiSign()) {
				jwt = withSignVerification(clientRegistration, accessTokenResponse);
			}
			else {
				jwt = withoutSignVerification(accessTokenResponse, clientRegistration);
			}

		}
		catch (JwtException ex) {
			OAuth2Error invalidIdTokenError = new OAuth2Error(INVALID_ID_TOKEN_ERROR_CODE, ex.getMessage(), null);
			throw new OAuth2AuthenticationException(invalidIdTokenError, invalidIdTokenError.toString(), ex);
		}
		catch (ParseException pe) {
			pe.printStackTrace();
		}
		OidcIdToken idToken = new OidcIdToken(jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt(),
				jwt.getClaims());
		return idToken;
	}

	private Jwt withSignVerification(ClientRegistration clientRegistration,
			OAuth2AccessTokenResponse accessTokenResponse) {
		Jwt jwt;
		JwtDecoder jwtDecoder = this.jwtDecoderFactory.createDecoder(clientRegistration);

		jwt = jwtDecoder
				.decode((String) accessTokenResponse.getAdditionalParameters().get(OidcParameterNames.ID_TOKEN));
		return jwt;
	}

	private Jwt withoutSignVerification(OAuth2AccessTokenResponse accessTokenResponse,
			ClientRegistration clientRegistration) throws ParseException {
		com.nimbusds.jwt.JWT jwt2 = JWTParser
				.parse((String) accessTokenResponse.getAdditionalParameters().get(OidcParameterNames.ID_TOKEN));
		Map<String, Object> headers = new LinkedHashMap(jwt2.getHeader().toJSONObject());
		JWTClaimsSet jwtClaimsSet = jwt2.getJWTClaimsSet();

		Map<String, Object> claims = (Map) this.claimSetConverter.convert(jwtClaimsSet.getClaims());
		Jwt jwt = Jwt
				.withTokenValue((String) accessTokenResponse.getAdditionalParameters().get(OidcParameterNames.ID_TOKEN))
				.headers((h) -> h.putAll(headers)).claims((c) -> c.putAll(claims)).build();

		try {
			this.validate(jwt, clientRegistration);
		}
		catch (JwtException ex) {
			OAuth2Error invalidIdTokenError = new OAuth2Error(INVALID_ID_TOKEN_ERROR_CODE, ex.getMessage(), null);
			throw new OAuth2AuthenticationException(invalidIdTokenError, invalidIdTokenError.toString(), ex);
		}
		return jwt;
	}

	static String createHash(String nonce) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] digest = md.digest(nonce.getBytes(StandardCharsets.US_ASCII));
		return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
	}

	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		Assert.notNull(userRequest, "userRequest cannot be null");
		OidcUserInfo userInfo = null;

		Set<GrantedAuthority> authorities = new LinkedHashSet<>();
		authorities.add(new OidcUserAuthority(userRequest.getIdToken(), userInfo));
		OAuth2AccessToken token = userRequest.getAccessToken();
		for (String authority : token.getScopes()) {
			authorities.add(new SimpleGrantedAuthority("SCOPE_" + authority));
		}

		OidcUser user;

		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
				.getUserNameAttributeName();
		if (StringUtils.hasText(userNameAttributeName)) {
			user = new DefaultOidcUser(authorities, userRequest.getIdToken(), userInfo, userNameAttributeName);
		}
		else {
			user = new DefaultOidcUser(authorities, userRequest.getIdToken(), userInfo);
		}

		return user;
	}

	private OAuth2TokenValidatorResult validate(Jwt jwt, ClientRegistration clientRegistration) {
		this.jwtCustomValidator.setClientRegistration(clientRegistration);
		OAuth2TokenValidatorResult result = this.jwtCustomValidator.validate(jwt);
		if (result.hasErrors()) {
			String description = (result.getErrors().iterator().next()).getDescription();
			throw new JwtValidationException(
					String.format("An error occurred while attempting to decode the Jwt: %s", description),
					result.getErrors());
		}
		else {
			return result;
		}
	}

}
