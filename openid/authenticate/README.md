## OIDC FLOW


 ![alt text](./doc/oidcFlow.png "")

 # Requisitos
 Para que funcione correctamente el IP Keycloak es necesario modificar el host para que resuelva el dominio keycloak a 127.0.0.1
 desde dentro de los contenedores se puede resolver el dominio pero para las redirecciones es necesario modificar el host para que resuelva.
 ´5
 # Flujo desde navegador
 Se accede a http://localhost:9090
 
 ![alt text](./doc/ClientFlow01.png "ClientFlow-01")
 
 Se presentan los distintos Identity Providers configurados.
 
 ![alt text](./doc/ClientFlow02.png "ClientFlow-02")
 Se selecciona para el ejemplo Keycloak que se inicia con el mismo job.
 
 ![alt text](./doc/ClientFlow03.png "ClientFlow-03")
 
 Se usa el usuario arturo/secret configurado en Keycloak
 
 ![alt text](./doc/ClientFlow04.png "ClientFlow-04")
 
 Se muestran los datos recuperados.
  
 ![alt text](./doc/ClientFlow05.png "ClientFlow-05")
 
 # Capturas de tráfico con wireshark de una solicitud correcta
 
![alt text](./doc/wireFlow01.png "flow")

![alt text](./doc/wireFlow02.png "flow")

![alt text](./doc/wireFlow03.png "flow")

![alt text](./doc/wireFlow04.png "flow")

![alt text](./doc/wireFlow05.png "flow")

![alt text](./doc/wireFlow06.png "flow")

![alt text](./doc/wireFlow07.png "flow")

![alt text](./doc/wireFlow08.png "flow")

![alt text](./doc/wireFlow09.png "flow")

![alt text](./doc/wireFlow10.png "flow")

![alt text](./doc/wireFlow11.png "flow")

![alt text](./doc/wireFlow12.png "flow")

![alt text](./doc/wireFlow13.png "flow")

![alt text](./doc/wireFlow14.png "flow")

![alt text](./doc/wireFlow15.png "flow")

![alt text](./doc/wireFlow16.png "flow")

![alt text](./doc/wireFlow17.png "flow")