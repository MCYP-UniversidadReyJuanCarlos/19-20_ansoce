## Requisitos
Para poder ejecutar correctamente los ejemplos se requiere mapear los dominios keycloak y client a 127.0.0.1

## Usuarios
Para logarse en la aplicación : user / password
Para acceder a keycloak: arturo/secret para la consola de administración admin/admin

## OpenID Connect

OpenId Connect es un protocolo de autenticación de usuarios que se basa en OpenId y extiende OAuth 2.0 dotándolo de una capa de autenticación de usuarios sin que estos últimos tengan que almacenar y gestionar las contraseñas.
Para poder usar estos proveedores de identidad primero es necesario registrar la aplicación, para obtener un identificador de cliente de la aplicación y una contraseña, para la realización de este trabajo se han selecciona tres proveedores, dos de ellos en la nube Google y OKTA y un tercero de instalación local KeyCloak
En la sección de bibliografía se dejan los enlaces a los tutoriales para la configuración de cada uno de los clientes:
 * [Configuración Google](https://support.google.com/a/answer/6349809)
 * [Configuración OKTA](https://developer.okta.com/docs/guides/)
 * [Configuración KeyCloak](https://www.keycloak.org/docs/latest/server_admin/)

Como OpenId Connect se basa en OAuth 2 es necesario importar en nuestros proyectos con Spring y SpringBoot al igual como se hacía en los ejemplos de OAuth la siguiente dependencia:

````xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
    <version>2.2.6.RELEASE</version>
</dependency>
````
La configuración de los proveedores es idéntica que, en los ejemplos de OAuth, para cualquier duda consultar estos ejemplos.
Para incorporar la autenticación contra el proveedor de identidad es necesario modificar la configuración de HttpSecurity, al igual que en los ejemplos de web se cambiaban los diferentes proveedores de BBDD en este caso se indica:

````java
   protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests().antMatchers("/js/*", "/css/*", "/images/*", "/font-awesome/**").permitAll()
            .anyRequest().fullyAuthenticated().and().logout().logoutSuccessHandler(oidcLogoutSuccessHandler())
            .invalidateHttpSession(true).deleteCookies("JSESSIONID").and().exceptionHandling()
            .accessDeniedPage("/accessDenied").and().oauth2Client().and().oauth2Login().userInfoEndpoint();
   }
````
Si se quiere gestionar el logout no solo de la aplicación si del IdP es necesario añadir un manejador para que realiza la llamada al endpoint de logout del IdP si lo posee

````java
   OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
      OidcClientInitiatedLogoutSuccessHandler successHandler = new OidcClientInitiatedLogoutSuccessHandler(
            this.clientRegistrationRepository);
      successHandler.setPostLogoutRedirectUri(URI.create("http://localhost:9090"));
      return successHandler;
   }

````