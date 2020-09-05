package es.urjc.tool;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.time.Instant;
import java.util.Date;

@RestController
public class WebController {

	private static final String DEFAULT_MESSAGE_DIGEST = "SHA-256";

	@Autowired
	private VariablesConfig variablesConfig;

	@PostMapping("/auth/realms/TFM/protocol/openid-connect/token")
	public AccessTokenResponse tokenWrongSing() {
		AccessTokenResponse at = new AccessTokenResponse();
		RSAPrivateKey key = getKey();
		RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey) key;
		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(rsaPrivateCrtKey.getModulus(),
				rsaPrivateCrtKey.getPublicExponent());
		// RSAPrivateKey key = getKey();
		String s = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJHRnlfTUVPY2RQeUpXUFVaVHM0M1JoZEZuRElhMWdLdzR6OElwSmlWNmxzIn0.eyJleHAiOjE1OTg2NDgzODcsImlhdCI6MTU5ODY0ODA4NywiYXV0aF90aW1lIjoxNTk4NjQ4MDg2LCJqdGkiOiJmZDdkODRjYy1mYzJlLTRhZjYtYmNmYS0zY2UxNTMzZGVjNjMiLCJpc3MiOiJodHRwOi8va2V5Y2xvYWs6ODA4MC9hdXRoL3JlYWxtcy9URk0iLCJhdWQiOlsibGlicmFyeS1zZXJ2aWNlIiwiYWNjb3VudCJdLCJzdWIiOiJhNjFkNzEwZi1jMjgxLTQ0OTktOGRmMy1lODkyYzBlZjk1NGMiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJvcGVuaWQtY2xpZW50Iiwibm9uY2UiOiJlUG5lTHNoeHhFczVnZHY1Nk93OEVIT2xObTFuZ2ZDeW1DX0ZvWmpJM0lFIiwic2Vzc2lvbl9zdGF0ZSI6ImU1MzVmYTk3LTQyMTYtNGZhNi05MzRiLTg5OTQ4Y2EzNWExZSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo5MDkwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwidXNlciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHVzZXIgZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiQXJ0dXJvIFNhbmNoZXogSnVpa28iLCJncm91cHMiOltdLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhcnR1cm8iLCJnaXZlbl9uYW1lIjoiQXJ0dXJvIiwiZmFtaWx5X25hbWUiOiJTYW5jaGV6IEp1aWtvIiwiZW1haWwiOiJhcnR1cm9AdXJqYy5lcyJ9.X2iD9jRptppZ7SVxM3IEdxc91LgNW_lW7lF5tb9RRWlHxng8F73mcZFprIEobyXaMkAt21nZ8F_DsoP9Xt9ldlkOEX3v6CLZlvmHwJ0a_EJUcrtosMnpn8MKX6ptCuyjDRslPoFjuv1D6OagvSLrQe_IwWg75O3TzCSZfJnSxMHpv8n67_tq-PbPWSeX21DjqaxzJGC_EOkcNY4KrwYUxRrDkdyICAZWDbcBuaNZcAVdBv71th48FpdHxP4oE0O30ZHr6UnDcgso3MUICRhcjPbzci25k1D-OaIAdUJpVB0fMLiQ8Cup4IqcMVMb7AYqtIJD4nVHrnYS01o57NkFF";
		String idToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJHRnlfTUVPY2RQeUpXUFVaVHM0M1JoZEZuRElhMWdLdzR6OElwSmlWNmxzIn0.eyJleHAiOjE1OTg2NDg5OTUsImlhdCI6MTU5ODY0ODY5NSwiYXV0aF90aW1lIjoxNTk4NjQ4Njg0LCJqdGkiOiI1MjhkMzdiOS0yZjQ1LTRhNTQtYjNjYS0wYWIwOTVlNTkxYzkiLCJpc3MiOiJodHRwOi8va2V5Y2xvYWs6ODA4MC9hdXRoL3JlYWxtcy9URk0iLCJhdWQiOiJvcGVuaWQtY2xpZW50Iiwic3ViIjoiYTYxZDcxMGYtYzI4MS00NDk5LThkZjMtZTg5MmMwZWY5NTRjIiwidHlwIjoiSUQiLCJhenAiOiJvcGVuaWQtY2xpZW50Iiwibm9uY2UiOiIyYmtNbnBMakNOT3pnd2RBQkhMVWNBN1N5QmxSc2hfRzI5N2RZMHNjS2s0Iiwic2Vzc2lvbl9zdGF0ZSI6IjI5ODY5MzhjLWQ4ZjMtNDE4ZS04OTE5LTk2MTJmNzBkYWVhMyIsImF0X2hhc2giOiJVR2J2NmlsVU9FZExHbTM1MUkzSXNBIiwiYWNyIjoiMSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJuYW1lIjoiQXJ0dXJvIFNhbmNoZXogSnVpa28iLCJncm91cHMiOltdLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhcnR1cm8iLCJnaXZlbl9uYW1lIjoiQXJ0dXJvIiwiZmFtaWx5X25hbWUiOiJTYW5jaGV6IEp1aWtvIiwiZW1haWwiOiJhcnR1cm9AdXJqYy5lcyJ9.NBVDt2764i6SniFTW7aGL4NeakPg54oYV_2AxoG2zA9p6ETCoEVlkB-KIdyxsBrHqEN9uTgYIY6V4GvIr-zzK4DPrs1WD4M_ekpspD8Q7cGmcetp2CNPOj-1Q5F7ZfV6buLfiP5BiI6ArK9gv3RUl5DSLL2HxPLHrPGWAfRAvvhrIZHiXtPMmIXW2XPuWNB08UAkTMPFNcae_GqdAGSjKIXnBts3BCHet2mQ4SLMB-KBJ4qOGDYx-9RHr7hupLpp544mco7pa_ANNk0SYcU1tntKtWar1-Qym2lKDS36jH86uX0rr7A0d4EqWBxvVfvOa2fkPa7IdAU-ug9c3m9Zxw";
		try {
			s = generateAccessToken(rsaPrivateCrtKey, publicKeySpec);
			idToken = generateIdToken(rsaPrivateCrtKey, publicKeySpec);

		}
		catch (JOSEException je) {
			je.printStackTrace();
		}
		catch (InvalidKeySpecException ise) {
			ise.printStackTrace();
		}
		catch (NoSuchAlgorithmException nse) {
			nse.printStackTrace();
		}
		catch (NoSuchProviderException nspe) {
			nspe.printStackTrace();
		}
		at.setToken(s);
		at.setExpiresIn(300L);
		at.setRefreshExpiresIn(1080L);
		at.setRefreshToken(
				"eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJmMDMxNDk0Ni1lZTEyLTRmYzEtYjdlMS1hN2Y4NjhlMTNiNGUifQ.eyJleHAiOjE1OTg2NDk4ODcsImlhdCI6MTU5ODY0ODA4NywianRpIjoiM2MwYzExMmItZDlkYi00ODY0LTljMGItYzk3MmY2YjMyMjBlIiwiaXNzIjoiaHR0cDovL2tleWNsb2FrOjgwODAvYXV0aC9yZWFsbXMvVEZNIiwiYXVkIjoiaHR0cDovL2tleWNsb2FrOjgwODAvYXV0aC9yZWFsbXMvVEZNIiwic3ViIjoiYTYxZDcxMGYtYzI4MS00NDk5LThkZjMtZTg5MmMwZWY5NTRjIiwidHlwIjoiUmVmcmVzaCIsImF6cCI6Im9wZW5pZC1jbGllbnQiLCJub25jZSI6ImVQbmVMc2h4eEVzNWdkdjU2T3c4RUhPbE5tMW5nZkN5bUNfRm9aakkzSUUiLCJzZXNzaW9uX3N0YXRlIjoiZTUzNWZhOTctNDIxNi00ZmE2LTkzNGItODk5NDhjYTM1YTFlIiwic2NvcGUiOiJvcGVuaWQgdXNlciBlbWFpbCBwcm9maWxlIn0.sKnWy1gO-ZObpS1xEEZSOsXgWL-lvPTGFICpCrLPlbM");
		at.setTokenType("bearer");
		at.setIdToken(idToken);
		at.setNotBeforePolicy(1598631554);
		at.setSessionState("e535fa97-4216-4fa6-934b-89948ca35a1e");
		at.setScope("openid user email profile");
		if (!this.variablesConfig.isTokenOk() && this.variablesConfig.isTokenBadSigned()) {
			at.setIdToken(at.getIdToken().substring(0, at.getIdToken().length() - 5));
			at.setToken(at.getToken().substring(0, at.getToken().length() - 5));
		}
		if (!this.variablesConfig.isTokenOk() && this.variablesConfig.isTokenExpired()) {
			try {
				Thread.sleep(2000);
			}
			catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
		return at;
	}

	public String generateAccessToken(RSAPrivateCrtKey rsaPrivateCrtKey, RSAPublicKeySpec publicKeySpec)
			throws JOSEException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {

		JWSHeader jwsHeader = generateHeader(publicKeySpec);

		JWTClaimsSet payload = new JWTClaimsSet.Builder().issuer("issuer").audience("audience").subject("subject")
				.expirationTime((!this.variablesConfig.isTokenOk() && this.variablesConfig.isTokenExpired())
						? Date.from(Instant.now().minusSeconds(3600)) : Date.from(Instant.now().plusSeconds(120)))
				.build();

		SignedJWT signedJWT = new SignedJWT(jwsHeader, payload);

		signedJWT.sign(new RSASSASigner(getKey()));
		String jwt = signedJWT.serialize();
		return jwt;
	}

	private JWSHeader generateHeader(RSAPublicKeySpec publicKeySpec)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		final PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
		return new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT)
				.keyID(Base64.encodeBase64URLSafeString(
						MessageDigest.getInstance(DEFAULT_MESSAGE_DIGEST).digest(publicKey.getEncoded())))
				.build();
	}

	public String generateIdToken(RSAPrivateCrtKey rsaPrivateCrtKey, RSAPublicKeySpec publicKeySpec)
			throws JOSEException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {

		JWSHeader jwsHeader = generateHeader(publicKeySpec);

		JWTClaimsSet payload = getIdTokenPayload();

		SignedJWT signedJWT = new SignedJWT(jwsHeader, payload);

		signedJWT.sign(new RSASSASigner(getKey()));
		String jwt = signedJWT.serialize();
		return jwt;
	}

	private RSAPrivateKey getKey() {
		Security.addProvider(new BouncyCastleProvider());
		byte[] der = Base64.decodeBase64(
				"MIIEpAIBAAKCAQEAvmMleyF/8RFqMPpORLyFAiDbdLbt2OHkRBkpuqGYWEJ/2L3wbItP2wNQiKmdMmmWiAqftFhryyXkJW23OGPTsnSDgRnB+De03D0bNwsvvDUk4ZomQ00vqOH+zz5S8cX9rOiBlXvJx4UARuvVRWl0INx0vTlL2GWsd9EcCpk49GB0i1NtqOiSc2J2TqNVetG/9J0bHmcWfHkqHzdzNLs+rfwPEA0GS+SZ1QahrYGiEE4qP7/cdV91TDZFTg8Bjk+odnq7Ne0kUx0j78V+RolFAB136mbQKKwgIpdOhMNZr/QAddetZbeqhNy+VpEovHm7j8gvH8K+gbUv0cF4fXM4QwIDAQABAoIBAQCWQdOT8SVzkaX4j6Pz9cEXo+zvmWvv7p/j6uQXKm/8wEb8PTOhxvU4Sc5mfU7WOaUXKyNOBzbTtszviT7AH3E1d2VOH5EJoJV5+DQhZg+/f0XjU3B9T06c//fFgk+eBv+lVj7nRjJC6EIGtxuz031WbgcoTC8C04OhcISsDK95lcTFGUjcayaqVhsP6aby+Bdk87LixHHvHaRCzLdpPI2050NpSmRGa4tZy0P9FddlNIq9gYE+65COk4cv3ACve/Zj57E5JkoLREZmle4NjLxPPIfyt4jliKEo4PmSH6+1T9juLgztc6Swqb7OUA9ciXV+1/P7P1KT9h4If9vT07TBAoGBAPB5mSLwRl7qT9Ugpdbp/ukO/M4rP7kURy671WFwZ0ALfdI8Csma5xiqX6jQ+U2UVyPjKpVL3KRbphTKQBvpHnjNR9LHdclbxHwaRusvBO2WYBdkO9fCV2dAo0HVncd1RLXtPsuNhZaxqrlr3TOcBC0ahY6Hwv4XPW8WuEDikHTjAoGBAMqtu92yRgR9E5KuwZ3h+XVIq0Ov5iwbmEZzA3b36ArsBGkVVRuUnbHDdVDAfWY2gTkzu/G6TEfmGso3x2QYnqxkebbKFTO1nHbwaa4IxMdovuaLOo7aP81rmL1xb5N7QJ2PcoN7HROFhEWL3Mb13Dj8EOVsta8gRvONqj2hSu0hAoGBALRhEcxSXQa/TYi5x4vw06Pv+ROQ/2iN3GMixAkl7lztAcEcBp95ERXAXqbbsdVOixrBQGTilau3j1mS6AnHkHLe0Jw7v499xWGtuDqbd1/b51JEflr2TOeHZa5xVLuTrCmmvu6ixmzl8gDJ9w/KXJuUhFAIZ+x+dDeK3ETXbbIvAoGAaB8AmE/xcHThDUYB6Z4hjYMbmNZQpPA4Nlj3urJ43sOJMILpChY7PQtwxV9eDJiLoltVfkV60qT/tksZf+619VJYxPLkGYB/NQsk/2c50Eebasyz9Jdil6FjAk+4RkfXVhpPdgRVV6YL4b6MWRoNXKkB9Rr361Pln1Fz1914pIECgYATvbdauZfGsMBN8zwxrP2CNsoBp9d65IyP092MI54Qi8BwDQ6JfvqlLas6gu8cVCxwAg4U8CC5v/RJYRd3LjKuaZuveoDD4NUIVi+FZaQP5NwdhpxvODcCCH45RmozSfQdcnCbtIVyddTqZ4pnMpS3k2Q2QSE1f+KkNuMkbjleqA==");
		PrivateKey privateKey = null;
		try {
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(der);
			KeyFactory kf = KeyFactory.getInstance("RSA", "BC");
			privateKey = kf.generatePrivate(spec);

		}
		catch (NoSuchAlgorithmException nscex) {
			nscex.printStackTrace();
		}
		catch (InvalidKeySpecException ivke) {
			ivke.printStackTrace();
		}
		catch (NoSuchProviderException nspe) {
			nspe.printStackTrace();
		}
		return (RSAPrivateKey) privateKey;
	}

	private JWTClaimsSet getIdTokenPayload() {

		JWTClaimsSet payload = new JWTClaimsSet.Builder()
				.expirationTime((!this.variablesConfig.isTokenOk() && this.variablesConfig.isTokenExpired())
						? Date.from(Instant.now().minusSeconds(3600)) : Date.from(Instant.now().plusSeconds(120)))
				.issueTime((!this.variablesConfig.isTokenOk() && this.variablesConfig.isTokenExpired())
						? Date.from(Instant.now().minusSeconds(3700)) : Date.from(Instant.now()))
				.issuer("http://keycloak:8080/auth/realms/TFM")
				.audience((!this.variablesConfig.isTokenOk() && this.variablesConfig.isTokenBadClaims())
						? "openid-client-bad" : "openid-client")
				.subject("a61d710f-c281-4499-8df3-e892c0ef954c").jwtID("9cc91a3b-31ec-44b5-8a50-5b74ecb66764")
				.claim("at_hash", "UGbv6ilUOEdLGm351I3IsA").claim("email_verified", true).claim("typ", "ID")
				.claim("preferred_username", "arturo").claim("given_name", "Arturo")
				.claim("nonce", "2bkMnpLjCNOzgwdABHLUcA7SyBlRsh_G297dY0scKk4").claim("acr", "1")
				.claim("azp", "openid-client").claim("auth_time", Date.from(Instant.now()))
				.claim("name", "Arturo Sanchez Juiko").claim("session_state", "2986938c-d8f3-418e-8919-9612f70daea3")
				.claim("family_name", "Sanchez Juiko").claim("email", "arturo@urjc.es").build();

		return payload;
	}

}