package org.asaunin.socialnetwork.web.dto;

import lombok.Getter;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;

import static org.asaunin.socialnetwork.config.Constants.AVATAR_FOLDER;

@Getter
public class PersonDTO {

	private static final Logger log = LoggerFactory.getLogger(PersonDTO.class);

	private Long id;
	private String avatar;
	private String pageAvatar;
	private String firstName;
	private String lastName;
	private String fullName;
	private String shortName;
	private String email;
	private String phone;
	private Date birthDate;
	private String gender;
	private Date created;
	private boolean isMyFriend;
	private boolean isFriendOfMine;

	public PersonDTO(Person entity) {
		final Person person = SecurityUtils.currentProfile();

		this.id = entity.getId();
		this.firstName = entity.getFirstName();
		this.lastName = entity.getLastName();
		this.fullName = entity.getFullName();
		this.shortName = entity.getShortName();
		this.email = entity.getEmail();
		this.phone = entity.getPhone();
		this.birthDate = entity.getBirthDate();
		this.gender = entity.getGender().toString();
		this.created = entity.getCreated();
		this.isMyFriend = entity.isFriendOf(person);
		this.isFriendOfMine = entity.hasFriend(person);

		setAvatar(id);
	}

	// TODO: 24.02.2017 Provide cache
	private void setAvatar(Long id) {
		final ClassLoader loader = getClass().getClassLoader();
		if (!new File(loader.getResource(AVATAR_FOLDER).getFile()).isDirectory()) {
			log.error("Image folder {} is not found", AVATAR_FOLDER);
			return;
		}

		final String empty = AVATAR_FOLDER + "undefined.gif";
		final String path = AVATAR_FOLDER + String.valueOf(id) + ".jpg";
		if (null!=loader.getResource(path)) {
			this.pageAvatar = path;
			this.avatar = path;
		} else {
			this.pageAvatar = empty;
			this.avatar = fullName;
		}
	}

}
