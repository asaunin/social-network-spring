package org.asaunin.socialnetwork.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static org.asaunin.socialnetwork.config.Constants.AVATAR_FOLDER;

// TODO: 24.02.2017 Provide cache
public final class AvatarService {

	private static final Logger log = LoggerFactory.getLogger(AvatarService.class);
	private static final ClassLoader loader = AvatarService.class.getClassLoader();

	private static AvatarService instance = new AvatarService();


	public static AvatarService getInstance() {
		return instance;
	}

	private AvatarService() {
		if (!new File(loader.getResource(AVATAR_FOLDER).getFile()).isDirectory()) {
			log.error("Avatar folder {} is not found", AVATAR_FOLDER);
		}
	}

	public static String getPageAvatar(Long id) {
		final String empty = AVATAR_FOLDER + "undefined.gif";
		final String path = AVATAR_FOLDER + String.valueOf(id) + ".jpg";

		if (null != loader.getResource(path)) {
			return path;
		}
		return empty;
	}

	public static String getAvatar(Long id, String fullName) {
		final String path = AVATAR_FOLDER + String.valueOf(id) + ".jpg";
		if (null != loader.getResource(path)) {
			return path;
		}
		return fullName;
	}

}
