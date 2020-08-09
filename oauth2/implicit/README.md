##Client Credentials Grant


Este tipo de autorización es una simplificación del Authorization Code, pero en vez de obtener un código y después validar se obtiene directamente el token de acceso sin ninguna verificación de que el cliente lo ha recibido.
Spring marca como deprecado este tipo de flujo por lo que no se ha implementado ningún caso de uso. 
[Referencia](https://tools.ietf.org/html/rfc6749#section-4.2)

     +----------+
     | Resource |
     |  Owner   |
     |          |
     +----------+
          ^
          |
         (B)
     +----|-----+          Client Identifier     +---------------+
     |         -+----(A)-- & Redirection URI --->|               |
     |  User-   |                                | Authorization |
     |  Agent  -|----(B)-- User authenticates -->|     Server    |
     |          |                                |               |
     |          |<---(C)--- Redirection URI ----<|               |
     |          |          with Access Token     +---------------+
     |          |            in Fragment
     |          |                                +---------------+
     |          |----(D)--- Redirection URI ---->|   Web-Hosted  |
     |          |          without Fragment      |     Client    |
     |          |                                |    Resource   |
     |     (F)  |<---(E)------- Script ---------<|               |
     |          |                                +---------------+
     +-|--------+
       |    |
      (A)  (G) Access Token
       |    |
       ^    v
     +---------+
     |         |
     |  Client |
     |         |
     +---------+
 
 - **(A)** El cliente inicia el flujo dirigiendo el navegador del propietario del recurso al punto final de autorización. El cliente incluye su identificador de cliente, permisos, estado local y un URI de redireccionamiento al que el servidor de autorización enviará de vuelta al agente de usuario una vez que se conceda (o deniegue el acceso). 

 - **(B)** El servidor de autorización autentica al propietario del recurso (a través del navegador) y establece si el propietario del recurso otorga o deniega la solicitud de acceso del cliente.

 - **(C)** Suponiendo que el propietario del recurso otorga acceso, el  servidor de autorización redirige al navegador de regreso al cliente utilizando el URI de redirección proporcionado anteriormente. El URI de redirección incluye el token de acceso en el fragmento de URI. 

 - **(D)** El navegador sigue las instrucciones de redirección al realizar una solicitud al recurso del cliente alojado en la web (que no incluye el fragmento por [ RFC2616 ]). El agente de usuario retiene la información del fragmento localmente. 

 - **(E)** El recurso de cliente alojado en la web devuelve una página web (normalmente un  documento HTML con un script incrustado) capaz de acceder a la URI de redireccionamiento completo que incluye el fragmento retenido por el agente de usuario y la extracción del token de acceso (y otros parámetros) contenidos en el fragmento. 

 - **(F)** El navegador ejecuta la secuencia de comandos proporcionada por el recurso de cliente alojado en la web localmente, que extrae el token de acceso. 

 - **(G)** El navegador pasa el token de acceso al cliente. 
