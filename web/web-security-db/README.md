# Ejemplo con formulario de login con SpringBoot usuarios personalizados en BBDD
En el resto de ejemplos anteriores se ha almacenado los usuarios siempre en memoria, esto no es un a situación real, sobre el ejemplo de configuración personalizada se realizan las modificaciones necesarias para almacenar los usuarios en una BBDD MySql.
 
En una BBDD nunca se deberían de almacenar las claves de los usuarios en claro siempre debería de estar almacenada un a hash robusto además usando un "salt" para dificultar más el descifrado en caso de comprometerse la hash.
Spring ofrece una solución muy simple para cumplir estos requisitos, para generar la hash de la clave se usara bcrypt. (referencia bcrypt)

Se crea la clase
````java
public class BcryptUtil {

    public static void main(String[] args) {
        BCryptPasswordEncoder pwd = new BCryptPasswordEncoder();
        System.out.println("primera iteracion");
        generate(pwd);
        System.out.println("Segunda iteracion");
        generate(pwd);
    }

    private static void generate(BCryptPasswordEncoder pwd) {
        System.out.println(String.format("password : %s",pwd.encode("password")));
        System.out.println(String.format("user : %s",pwd.encode("user")));
        System.out.println(String.format("admin : %s",pwd.encode("admin")));
    }

}
````

Se ejecuta y se comprueba que la clave generada es distinta en cada iteración:

````shell script
primera iteración
password : $2a$10$mIb/bICwTrcZNgn0wB565ev3lM7mOePDS/mliTYc0lepext6n7F.a
user : $2a$10$ZaQiIk5engIQq2cy2TQ5megObLH9fTOOQfxCxQVoh8cVDlybaZ0su
admin : $2a$10$CNPpPTLGLNvO.O4t/Io5S.JZYpI7/X/5kiORP0VcKnKLnIRb3CLSe
Segunda iteración
password : $2a$10$L.8NPaBSuY.sPXconDcgz.1xqPQq8hCZQk51SjYHXiNdbGAdqLDj.
user : $2a$10$L/sN1YeEuo3GdUOec8UCN.KFPJElkahH.KY1qoib1IimbIP2QMa9.
admin : $2a$10$hXBsQK2nJOF6C9WtgtcVVOudccJxB6Pi5IBs/3n8PqOF3yYladSTq

Process finished with exit code 0
````

como base de datos se usa MySql, para evitar instalaciones se usaran contenedores tanto para levantar la BBDD para persistir el modelo de seguridad como para la aplicación.
En la BBDD se crean dos tablas users y authorities, que son las tablas pro defecto que usa Spring, la primera de ellas define a los usuarios mientras que la segunda establece los roles que cada uno de los usuarios tiene por lo tanto se habla de una relación de 1 a N, un usuario puede tener N roles. Es importante que los usuarios tengan roles asignados ya que con ellos se podrá realizar un grano más fino de permisos.

Se modifica la clase BasicConfiguration.java
````java
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.jdbcAuthentication().passwordEncoder(passwordEncoder()).dataSource(dataSource);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
````
Con este pequeño cambio se indica a spring que los usuarios se deben de validar contra el modelo de datos y además las contraseñas están encriptadas con el bcrypt.
Para configurar la BBDD en Spring tan solo hay que indicarle las siguientes propiedades en el application.properties

````properties
spring.datasource.url=jdbc:mysql://mysql-service:3306/jdbc_authentication
spring.datasource.username=root
spring.datasource.password=pass
spring.datasource.initialization-mode=always
spring.jpa.hibernate.ddl-auto=none
````

