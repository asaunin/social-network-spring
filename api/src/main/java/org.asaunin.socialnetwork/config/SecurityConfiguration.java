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
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.asaunin.socialnetwork.config.Constants.*;

@Configuration
@EnableWebSecurity
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER) // TODO: Enables h2 console - only for development environment
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private CsrfHeaderFilter csrfFilter;

    @Autowired
    public SecurityConfiguration(UserDetailsServiceImpl userDetailsService, CsrfHeaderFilter csrfFilter) {
        this.userDetailsService = userDetailsService;
        this.csrfFilter = csrfFilter;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/**/*.js")
                .antMatchers("/**/*.ico")
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
                .deleteCookies(REMEMBER_ME_COOKIE)
				.permitAll()
				.and()
			.headers() // TODO: Enables h2 console - only for development environment
				.frameOptions()
				.disable()
				.and()
			.rememberMe()
				.rememberMeServices(rememberMeService())
				.key(REMEMBER_ME_TOKEN)
				.and()
			.exceptionHandling()
                .authenticationEntryPoint(new Http401AuthenticationEntryPoint("Access Denied"))
				.and()
			.csrf()
        		.csrfTokenRepository(csrfTokenRepository())
				.and()
            .cors()
                .and()
            .addFilterAfter(csrfFilter, CsrfFilter.class)
		;
		// @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName(XSRF_TOKEN_HEADER_NAME);
        return repository;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenBasedRememberMeServices rememberMeService() {
        final TokenBasedRememberMeServices services =
                new TokenBasedRememberMeServices(REMEMBER_ME_TOKEN, userDetailsService);

        services.setCookieName(REMEMBER_ME_COOKIE);
        services.setTokenValiditySeconds(3600);
        services.setAlwaysRemember(true);

        return services;
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("OPTIONS");
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }

}