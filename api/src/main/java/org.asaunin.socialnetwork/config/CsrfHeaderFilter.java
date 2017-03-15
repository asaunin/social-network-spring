package org.asaunin.socialnetwork.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.asaunin.socialnetwork.config.Constants.XSRF_TOKEN_COOKIE_NAME;
import static org.asaunin.socialnetwork.config.Constants.XSRF_TOKEN_HEADER_NAME;

@Configuration
public class CsrfHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrf != null) {
            Cookie cookie = WebUtils.getCookie(request, XSRF_TOKEN_COOKIE_NAME);
            String token = csrf.getToken();
            if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                cookie = new Cookie(XSRF_TOKEN_COOKIE_NAME, token);
                cookie.setPath("/");
                response.addCookie(cookie);
                response.addHeader(XSRF_TOKEN_HEADER_NAME, token);
            }
        }
        filterChain.doFilter(request, response);
    }


}