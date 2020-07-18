# Spring-Security 
[![Actions Status](https://github.com/MCYP-UniversidadReyJuanCarlos/19-20_ansoce/workflows/maven/badge.svg)](https://github.com/MCYP-UniversidadReyJuanCarlos/19-20_ansoce/actions)


* **Project title** : Comparativa de métodos de autenticación y autorización en aplicaciones Spring
* **Project description** :El alumno implementará y comparará diferentes técnicas de autenticación y autorización disponibles dentro del framework de desarrollo Spring. Concretamente se analizarán Basic Auth, JWT y OAUTH 2. Se realizarán pruebas de seguridad de las técnicas implementadas y se discutirán ventajas y desventajas de los tres en base a diferentes criterios. 
* Features :
  * Web
    * context
    * config  
    * ejemplos
      * [Aplicacion Web básica](web/web-basic)
      * [Aplicacion Web Securizada básica](web/web-basic-security)
      * [users in file](web/web-security-custom-I)
      * [users in DDBB](web/web-security-db)
      * [users in DDBB noSQL](web/web-security-nosql)
      * [users in ldap](web/web-security-ldap)
  * OAUTH (Spring OAUH Client, Resource Server)
  * JWT (Spring JOSE)
  * OpenID
* How to run
Cada uno de los proyectos se puede lanzar individualmente con su docker-compose con el comando:
````shell script
docker-compose up -d
````
y para parar con
````shell script
docker-compose down
```` 
También se integra un jenkins con una serie de tareas que permiten lanzar cada uno de los ejemplos de forma sencilla. Para arrancar el jenkins se ejecuta en la raíz del proyecto:

````shell script
docker-compose up -d
````
y para parar con
````shell script
docker-compose down
```` 
La clave por defecto de jenkins es admin/adminpass.
* Basic usage
* Development documentation
* Architecture
* Prepare development/execution environment
