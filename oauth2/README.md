# Modulo OAuth 2.0

## Usuarios
Para logarse en la aplicación : user / password
Para acceder al proveedor de autenticación: alumno@urjc.es/password

## ¿Que es OAUTH 2.0?
OAuth 2.0 (Open Authorization 2.0) es un framework de autorización que permite a aplicaciones de terceros obtener acceso limitado a un servicio HTTP, en nombre del propietario del recurso al que se accede previa gestión de consentimiento entre dicho propietario del recurso y el servicio HTTP, o permitiendo a la aplicación de terceros obtener acceso en su propio nombre.
[link](https://tools.ietf.org/html/rfc6749)

## ¿Que no es OAUTH 2.0?
Se deben diferenciar dos conceptos: **autorización** y **autenticación**:
 - Autenticación: Proceso por el cual se verifica quién eres (Identificación)
 - Autorización: Proceso por el cual se obtiene que puedes hacer (Permisos)
 con estos términos definidos OAuth no es un protocolo de autenticación como se ha definido anteriormente es un framework que permite delegar la autorización.
 
## Ventajas
 - Protocolo muy flexible
 - Acceso limitado, los tokens manejados caducan y con los scopes limitan la funcionalidad.
 - Permite compartir información de forma anónima, los token no contienen información de los usuarios.
 - Gran comunidad e integración con multitud de frameworks y SDKs.
 
 
## Actores en OAuth 2.0
 - Propietario del recurso (Resource Owner): El usuario que cuenta con la propiedad de los recursos a los que se quiere acceder
 - Aplicación cliente (Client): Aplicación que realiza las consultas a los recursos en nombre del propietario del recurso.
 - Servidor de autorización (Authorization Server): El servidor que emite tokens de acceso al cliente después de haber realizado correctamente autenticar al propietario del recurso y obtener autorización.
 - Servidor de recursos (Resource Server): Servidor que expone los datos a los que se quiere acceder.
 
## Flujos de OAuth
El esquema siguiente muestra un flujo abstracto de interacción entre los 4 roles

     +--------+                               +---------------+
     |        |--(A)- Authorization Request ->|   Resource    |
     |        |                               |     Owner     |
     |        |<-(B)-- Authorization Grant ---|               |
     |        |                               +---------------+
     |        |
     |        |                               +---------------+
     |        |--(C)-- Authorization Grant -->| Authorization |
     | Client |                               |     Server    |
     |        |<-(D)----- Access Token -------|               |
     |        |                               +---------------+
     |        |
     |        |                               +---------------+
     |        |--(E)----- Access Token ------>|    Resource   |
     |        |                               |     Server    |
     |        |<-(F)--- Protected Resource ---|               |
     +--------+                               +---------------+
     
Referencia en [link](https://tools.ietf.org/html/rfc6749#section-1.2)
     
## Tipos (grant type)     
 - [Client credentials](clientcredentials/README.md)
 - [Resource Owner Password](ownerpassword/README.md)
 - [Authorization code](authorizationcode/README.md)
 - [Implicit](implicit/README.md)

## Configurar cliente

Para que la aplicación sea segura, simplemente se debe agregar Spring Security como dependencia.
Dado que desea realizar una autorización mediante OAuth 2.0 es necesario incluir la siguiente dependencia para que la aplicación Spring con SpringBoot se integre con el  servidor de autorización
````xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
````

El siguiente paso es configurar el proveedor de autorización que se desee, en este caso para los ejemplos se ha creado uno a modo de ejemplo que se despliega con los ejemplos:
`````
spring:
  security:
    oauth2:
      client:
        registration:
          custom-client:
            provider: custom-provider
            client-id: user
            client-secret: secret
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/mensaje"
            scope: user
        provider:
          custom-provider:
            token-uri: http://authorizationserver:8081/oauth/token
            authorization-uri: http://localhost:8081/oauth/authorize
`````
Spring tiene una clase con proveedores de OAuth habituales que facilita la configuración, esta clase es:
org.springframework.security.config.oauth2.client.CommonOAuth2Provider [link](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/oauth2/client/CommonOAuth2Provider.html). A modo de ejemplo:

````java
   GITHUB {

      @Override
      public Builder getBuilder(String registrationId) {
         ClientRegistration.Builder builder = getBuilder(registrationId,
               ClientAuthenticationMethod.BASIC, DEFAULT_REDIRECT_URL);
         builder.scope("read:user");
         builder.authorizationUri("https://github.com/login/oauth/authorize");
         builder.tokenUri("https://github.com/login/oauth/access_token");
         builder.userInfoUri("https://api.github.com/user");
         builder.userNameAttributeName("id");
         builder.clientName("GitHub");
         return builder;
      }
   },
````

Si se quisiera usar GitHub como proveedor bastaría con indicarle el cliente y la clave:

````
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            clientId: github-client-id
            clientSecret: github-client-secret
````

Un ejemplo completo con diferentes funcionalidades se puede encontrar en las guías de Spring [link](https://spring.io/guides/tutorials/spring-boot-oauth2/).

En los diferentes ejemplos que se han generado, la aplicación gestiona la autenticación con usuarios en memoria y es a la hora de consumir los recursos expuestos por un servidor de recursos cuando se necesita obtener el token de autorización. Para ello se configura un cliente con el módulo de Spring WebFlux.

Cuando la aplicación recibe una petición para llamar al servidor de recursos utiliza el siguiente bean:

````java
   @Bean
   WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
      ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
            authorizedClientManager);
      return WebClient.builder().apply(oauth2Client.oauth2Configuration()).build();
   }

   @Bean
   OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
         OAuth2AuthorizedClientRepository authorizedClientRepository) {
      OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
            .authorizationCode().refreshToken().clientCredentials().password().build();
      DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
            clientRegistrationRepository, authorizedClientRepository);
      authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

      authorizedClientManager.setContextAttributesMapper(contextAttributesMapper());

      return authorizedClientManager;
````

Con Este Bean Spring recopila los proveedores configurados y habilita los flujos de OAuth que se quiera, en este caso como se observa en el código Authorization Code, Client Credential, Resourse owner password y refresh token
Con esta configuración cuando la aplicación quiera llamar a un recurso externo, verificara si tiene un token valido y si no es así inicia el flujo para obtenerlo, una vez obtenido lo envía al destino como cabecera.

````java

      this.webClient.get().uri(Urls de recurso)
            .attributes(
                  ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId(clientRegistrationId))
            .retrieve()
            .onStatus((s) -> s.equals(HttpStatus.UNAUTHORIZED),
                  (cr) -> Mono.just(new BadCredentialsException("Not authenticated")))
            .onStatus(HttpStatus::is4xxClientError,
                  (cr) -> Mono.just(new IllegalArgumentException(cr.statusCode().getReasonPhrase())))
            .onStatus(HttpStatus::is5xxServerError,
                  (cr) -> Mono.just(new Exception(cr.statusCode().getReasonPhrase())))
            .bodyToMono(String.class).block();
````

En función del flujo que se quiera realizar se requiere modificaciones en la configuración.

[Link](https://docs.spring.io/spring-security/site/docs/5.1.0.RELEASE/reference/html/webflux-oauth2.html) a las guías de Spring

