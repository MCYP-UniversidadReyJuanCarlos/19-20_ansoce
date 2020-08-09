# Modulo OAuth 2.0
## ¿Que es OAUTH 2.0?
OAuth 2.0 (Open Authorization 2.0) es un framework de autorización que permite a aplicaciones de terceros obtener acceso limitado a un servicio HTTP, en nombre del propietario del recurso al que se accede previa gestión de consentimiento entre dicho propietario del recurso y el servicio HTTP, o permitiendo a la aplicación de terceros obtener acceso en su propio nombre.
[link](https://tools.ietf.org/html/rfc6749)

## ¿Que no es OAUTH 2.0?
Se deben diferenciar dos conceptos: **autorización** y **autenticación**:
 - Autenticación: Proceso por el cual se verifica quien eres (Identificación)
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



