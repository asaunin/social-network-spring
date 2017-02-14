package org.asaunin.socialnetwork.config;

import org.asaunin.socialnetwork.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity()
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired private UserDetailsServiceImpl userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.csrf()
				.disable()
//			.authorizeRequests()
//				.antMatchers("/**/*.{js,html,css}").permitAll()
//				.antMatchers("/images/**").permitAll()
//				.antMatchers("/").permitAll()
//				.anyRequest().authenticated()
//				.and()
			.formLogin()
				.loginPage("/");
		// @formatter:on
	}
}