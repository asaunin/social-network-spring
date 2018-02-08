package org.asaunin.socialnetwork.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public final class Constants {

    private static final Logger log = LoggerFactory.getLogger(Constants.class);

    public static final String URI_API_PREFIX = "/api";
    public static final String URI_MESSAGES = URI_API_PREFIX + "/messages";
    public static final String URI_SOCIAL = URI_API_PREFIX + "/social";

    public static final String ERROR_UPDATE_PROFILE = "Updating profile doesn't match the current one";
    public static final String ERROR_UPDATE_EMAIL = "E-mail is already used by another person";
    public static final String ERROR_SIGN_UP_EMAIL = ERROR_UPDATE_EMAIL;
    public static final String ERROR_PASSWORD_CONFIRMATION = "Current password is invalid";

    public static String API_URL;
    public static String WEB_URL;
    public static String AVATAR_FOLDER;

    static String REMEMBER_ME_TOKEN;
    static String REMEMBER_ME_COOKIE;

    static final String XSRF_TOKEN_COOKIE_NAME = "XSRF-TOKEN";
    static final String XSRF_TOKEN_HEADER_NAME = "X-XSRF-TOKEN";

    @Autowired
    private Constants(Environment environment) {
        try {
            API_URL = environment.getProperty("resources.api-url");
            WEB_URL = environment.getProperty("resources.web-url");
        } catch (Exception ignored) {}

        if (API_URL != null) {
            log.debug("Set API url: {}", API_URL);
        } else {
            API_URL = "http://localhost:8080";
            log.warn("API url is not configured. Set to the default one: {}", API_URL);
        }

        if (WEB_URL != null) {
            log.debug("Set WEB url: {}", WEB_URL);
        } else {
            WEB_URL = "http://localhost:8080";
            log.warn("WEB url is not configured. Set to the default one: {}", WEB_URL);
        }
    }

    @Value("${resources.avatar-folder}")
    public void setImageFolder(String path) {
        AVATAR_FOLDER = path;
    }

    @Value("${remember-me.token}")
    public void setRememberMeToken(String token) {
        REMEMBER_ME_TOKEN = token;
    }

    @Value("${remember-me.cookie}")
    public void setRememberMeCookie(String cookie) {
        REMEMBER_ME_COOKIE = cookie;
    }

}
