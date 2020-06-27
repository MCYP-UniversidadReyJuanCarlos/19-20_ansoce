package es.urjc.webcustom.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

final class BcryptUtil {

	private BcryptUtil() {
	}

	private static void main(String[] args) {
		BCryptPasswordEncoder pwd = new BCryptPasswordEncoder();
		System.out.println("primera iteracion");
		generate(pwd);
		System.out.println("Segunda iteracion");
		generate(pwd);
	}

	private static void generate(BCryptPasswordEncoder pwd) {
		System.out.println(String.format("password : %s", pwd.encode("password")));
		System.out.println(String.format("user : %s", pwd.encode("user")));
		System.out.println(String.format("admin : %s", pwd.encode("admin")));
	}

}
