package es.urjc.oauth2.authserv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
class UserConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().mvcMatchers("/.well-known/jwks.json").permitAll().anyRequest().authenticated().and()
				.httpBasic().and().csrf()
				.ignoringRequestMatchers((request) -> "/introspect".equals(request.getRequestURI()));
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		return new InMemoryUserDetailsManager(User.withDefaultPasswordEncoder().username("alumno@urjc.es")
				.password("password").roles("user").build());
	}

}
