package org.asaunin.socialnetwork.web.dto;

import lombok.Getter;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.security.SecurityUtils;
import org.asaunin.socialnetwork.service.AvatarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

@Getter
public class PersonDTO implements Serializable {

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

	public PersonDTO(Person person) {
		final Person profile = SecurityUtils.currentProfile();

		this.id = person.getId();
		this.firstName = person.getFirstName();
		this.lastName = person.getLastName();
		this.fullName = person.getFullName();
		this.shortName = person.getShortName();
		this.email = person.getEmail();
		this.phone = person.getPhone();
		this.birthDate = person.getBirthDate();
		this.gender = person.getGender().toString();
		this.created = person.getCreated();
		this.isMyFriend = person.isFriendOf(profile);
		this.isFriendOfMine = person.hasFriend(profile);
		this.pageAvatar = AvatarService.getPageAvatar(person.getId());
		this.avatar = AvatarService.getAvatar(person.getId(), person.getFullName());
	}

}
