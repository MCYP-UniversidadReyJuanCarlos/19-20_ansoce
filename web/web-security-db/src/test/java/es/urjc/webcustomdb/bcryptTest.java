package es.urjc.webcustomdb;

import junit.framework.TestCase;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class bcryptTest extends TestCase {

    public void generatePwd (){
        BCryptPasswordEncoder pwd = new BCryptPasswordEncoder();
        System.out.println(String.format("password : %s",pwd.encode("password")));
        System.out.println(String.format("user : %s",pwd.encode("user")));
        System.out.println(String.format("admin : %s",pwd.encode("admin")));
    }
}
