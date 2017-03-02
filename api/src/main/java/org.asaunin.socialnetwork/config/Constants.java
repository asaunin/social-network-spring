package org.asaunin.socialnetwork.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class Constants {

    public static final String URI_API_PREFIX = "/api";
    public static final String URI_MESSAGES = "/messages";

    public static final String ERROR_UPDATE_PROFILE = "Updating profile doesn't match the current one";
    public static final String ERROR_UPDATE_EMAIL = "E-mail is already used by another person";
    public static final String ERROR_SIGN_UP_EMAIL = ERROR_UPDATE_EMAIL;

    public static String AVATAR_FOLDER;

    @Value("${resources.avatar-folder}")
    public void setImageFolder(String path) {
        AVATAR_FOLDER = path;
    }

    private Constants() {}

}
