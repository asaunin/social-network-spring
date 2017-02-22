package org.asaunin.socialnetwork.security;

import org.asaunin.socialnetwork.domain.Person;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class SecurityUtils {

	private SecurityUtils() {
	}

	public static String currentLogin() {
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		final Authentication authentication = securityContext.getAuthentication();
		String login = null;
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				final UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
				login = springSecurityUser.getUsername();
			} else if (authentication.getPrincipal() instanceof String) {
				login = (String) authentication.getPrincipal();
			}
		}
		return login;
	}

	public static Person currentProfile() {
		final SecurityContext securityContext = SecurityContextHolder.getContext();
		final Authentication authentication = securityContext.getAuthentication();
		Person profile = null;
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				profile = (Person) authentication.getPrincipal();
			}
		}
		return profile;
	}

}
