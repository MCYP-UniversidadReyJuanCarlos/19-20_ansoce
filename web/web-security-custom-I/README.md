# Ejemplo con formulario de login con SpringBoot usuarios personalizados
Basándonos en el ejemplo "Ejemplo con formulario de login y SpringBoot" se añade una mínima personalizacion sobre la gestion de usuarios ademas de configurar el servidor para funcionar en https para evitar atques MITM.

Se elimina la opcion de autonfigurado de seguridad que por defecto viene con el moduflo de seguridad para ello se puede hacer uso del aplication propertie o bien por anotaciones, en este caso se hara de esta ultima forma. Se añade la siguiente excepcion a la clase WebApplication.java.

```java
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
```
