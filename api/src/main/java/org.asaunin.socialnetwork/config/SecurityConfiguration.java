package org.asaunin.socialnetwork.config;

import org.asaunin.socialnetwork.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.asaunin.socialnetwork.config.Constants.AVATAR_FOLDER;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.antMatchers("/**/*.js")
				.antMatchers("/**/*.css")
				.antMatchers("/**/*.html")
				.antMatchers("/bootstrap/**")
				.antMatchers("/" + AVATAR_FOLDER + "undefined.gif")
		;
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
				.antMatchers("/").permitAll()
				.antMatchers("/api/login").permitAll()
				.antMatchers("/api/**").authenticated() //Redundant
                .anyRequest().authenticated()
				.and()
			.logout()
				.logoutUrl("/api/logout")
				.permitAll()
				.and()
//			.rememberMe()
//				.disable()
			.exceptionHandling()
                .authenticationEntryPoint(new Http401AuthenticationEntryPoint("Access Denied"))
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
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}