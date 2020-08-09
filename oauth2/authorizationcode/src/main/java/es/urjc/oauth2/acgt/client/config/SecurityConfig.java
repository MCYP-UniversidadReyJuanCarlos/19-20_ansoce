package es.urjc.oauth2.acgt.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/webjars/**");

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		 * http.authorizeRequests().anyRequest()
		 * .authenticated().and().formLogin().loginPage("/login")
		 * .failureUrl("/login-error").permitAll().and().oauth2Client();
		 */

		http.authorizeRequests().antMatchers("/index", "/js/*", "/css/*", "/images/*", "/font-awesome/**").permitAll()
				.anyRequest().authenticated().and().formLogin().loginPage("/login").failureUrl("/login-error")
				.permitAll().and().logout().logoutUrl("/logout").logoutSuccessUrl("/login").invalidateHttpSession(true)
				.deleteCookies("JSESSIONID").and().oauth2Client().and().exceptionHandling()
				.accessDeniedPage("/accessDenied");

	}

	@Bean
	public UserDetailsService users() {
		UserDetails user = User.withDefaultPasswordEncoder().username("user").password("password").roles("user")
				.build();
		return new InMemoryUserDetailsManager(user);
	}

}
