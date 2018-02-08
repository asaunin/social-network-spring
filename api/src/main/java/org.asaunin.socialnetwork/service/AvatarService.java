package org.asaunin.socialnetwork.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

import static org.asaunin.socialnetwork.config.Constants.API_URL;
import static org.asaunin.socialnetwork.config.Constants.AVATAR_FOLDER;

// TODO: 24.02.2017 Provide cache
@Service
public final class AvatarService {

    private static final Logger log = LoggerFactory.getLogger(AvatarService.class);
    private static final ClassLoader loader = AvatarService.class.getClassLoader();

    private AvatarService() {
        try {
            final File folder = new File(loader.getResource(AVATAR_FOLDER).getFile());
            if (folder.isDirectory()) {
                log.info("Avatar folder {} was found", AVATAR_FOLDER);
            } else {
                log.error("Avatar folder {} was not found", AVATAR_FOLDER);
            }
        } catch (NullPointerException ex) {
            log.error("Avatar folder is not found: {}", ex);
        }
    }

    public static String getPageAvatar(Long id) {
        final String path = AVATAR_FOLDER + String.valueOf(id) + ".jpg";

        if (null != loader.getResource(path)) {
            return API_URL + "/" + path;
        }
        return API_URL + "/" + AVATAR_FOLDER + "undefined.gif";
    }

    public static String getAvatar(Long id, String fullName) {
        final String path = AVATAR_FOLDER + String.valueOf(id) + ".jpg";
        if (null != loader.getResource(path)) {
            return API_URL + "/" + path;
        }
        return fullName;
    }

}
