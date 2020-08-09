## Resource Owner Password Credentials Grant

En "Resource Owner Password Credentials Grant" el usuario debe de intervenir proporcionando su usuario y contraseña no al servidor de autorización si no a la aplicación cliente.
Esta situación implica que los dueños del recurso deben confiar plenamente en la aplicación para introducir sus credenciales.
[Referencia](https://tools.ietf.org/html/rfc6749#section-4.3)

     +----------+
     | Resource |
     |  Owner   |
     |          |
     +----------+
          v
          |    Resource Owner
         (A) Password Credentials
          |
          v
     +---------+                                  +---------------+
     |         |>--(B)---- Resource Owner ------->|               |
     |         |         Password Credentials     | Authorization |
     | Client  |                                  |     Server    |
     |         |<--(C)---- Access Token ---------<|               |
     |         |    (w/ Optional Refresh Token)   |               |
     +---------+                                  +---------------+
 
 - **(A)** El propietario del recurso proporciona al cliente su nombre de usuario y contraseña. 

 - **(B)** El cliente solicita un token de acceso desde el punto final del token del servidor de autorización al incluir las credenciales recibidas del propietario del recurso. Al realizar la solicitud, el cliente se autentica con el servidor de autorización. 

 - **(C)** El servidor de autorización autentica al cliente y valida las credenciales del propietario del recurso y, si es válido, emite un token de acceso
 
 
# Flujo desde navegador
Se accede a http://127.0.0.1:9090

![alt text](./doc/clientApp01.png "App-01")

Se loga en la aplicación con user/password

![alt text](./doc/clientApp02.png "App-02")

![alt text](./doc/clientApp03.png "App-03")

Se rellenan las credenciales de usuario para solicitar el mensaje al servidor de recursos

![alt text](./doc/clientApp04.png "App-04")

Se Obtiene el mensaje solicitado

![alt text](./doc/clientApp05.png "App-05")

# Capturas de tráfico con wireshark de una solicitud correcta

![alt text](./doc/wireFlow01.png "flow")

![alt text](./doc/wireFlow02.png "flow")

![alt text](./doc/wireFlow03.png "flow")

![alt text](./doc/wireFlow04.png "flow")
