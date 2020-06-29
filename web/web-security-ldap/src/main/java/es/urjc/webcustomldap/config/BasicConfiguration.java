package es.urjc.webcustomldap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class BasicConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.ldapAuthentication().userDnPatterns("uid={0},ou=people").groupSearchBase("ou=groups").contextSource()
				.url("ldap://localhost:8389/dc=springframework,dc=org").and().passwordCompare()
				.passwordEncoder(passwordEncoder()).passwordAttribute("userPassword");

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().and().requiresChannel().anyRequest().requiresSecure().and().authorizeRequests()
				.antMatchers("/index", "/js/*", "/css/*", "/images/*", "/font-awesome/**").permitAll()
				.antMatchers("/admin").access("hasRole('ROLE_MANAGERS')").anyRequest().authenticated().and().formLogin()
				.loginPage("/login").permitAll().and().logout().logoutUrl("/logout").logoutSuccessUrl("/login")
				.invalidateHttpSession(true).deleteCookies("JSESSIONID").and().exceptionHandling()
				.accessDeniedPage("/accessDenied");

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
