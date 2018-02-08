package org.asaunin.socialnetwork.security;

import org.asaunin.socialnetwork.config.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

@Service
public class SocialSignInAdapter implements SignInAdapter {

    private static final Logger log = LoggerFactory.getLogger(SocialSignInAdapter.class);

    private final UserDetailsService userDetailsService;

    public SocialSignInAdapter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        final UserDetails user = userDetailsService.loadUserByUsername(userId);
        final Authentication newAuth = new UsernamePasswordAuthenticationToken(
            user,
            null,
            user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return Constants.WEB_URL + "/#/profile";
    }
}
