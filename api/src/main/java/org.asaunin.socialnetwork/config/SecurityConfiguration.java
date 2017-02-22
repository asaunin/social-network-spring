package org.asaunin.socialnetwork.config;

import org.asaunin.socialnetwork.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.httpBasic()
				.and()
			.csrf()
				.disable()
			.authorizeRequests()
				.antMatchers("/**/*.{js,html,css}").permitAll()
				.antMatchers("/images/**").permitAll()
				.antMatchers("/api/**").authenticated()
				.and()
			.formLogin()
				.loginPage("/api/login")
				.permitAll()
				.and()
			.logout()
				.logoutUrl("/api/logout")
				.permitAll()
				.and()
//			.rememberMe()
//				.disable()
			.exceptionHandling()
                .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
				.and()
		// TODO: 22.02.2017 Add remember me
		// TODO: 22.02.2017 Add tokens
		// TODO: 22.02.2017 Add registration form
		;
		// @formatter:on
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(userDetailsService)
				.passwordEncoder(bCryptPasswordEncoder());
//		auth.authenticationProvider(rememberMeAuthenticationProvider);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

}