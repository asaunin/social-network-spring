package org.asaunin.socialnetwork.config;

import org.asaunin.socialnetwork.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.asaunin.socialnetwork.config.Constants.AVATAR_FOLDER;

@Configuration
@EnableWebSecurity
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) // TODO: Enables h2 console - only for development environment
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
				.antMatchers("/console/**").permitAll() // TODO: Enables h2 console - only for development environment
				.antMatchers("/api/login").permitAll()
				.antMatchers("/api/signUp").permitAll()
				.antMatchers("/api/**").authenticated() //Redundant
                .anyRequest().authenticated()
				.and()
			.logout()
				.logoutUrl("/api/logout")
				.permitAll()
				.and()
			.headers() // TODO: Enables h2 console - only for development environment
				.frameOptions()
				.disable()
				.and()
//			.rememberMe()
//				.disable()
			.exceptionHandling()
                .authenticationEntryPoint(new Http401AuthenticationEntryPoint("Access Denied"))
				.and()
		// TODO: 22.02.2017 Add remember me
		// TODO: 22.02.2017 Add tokens
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