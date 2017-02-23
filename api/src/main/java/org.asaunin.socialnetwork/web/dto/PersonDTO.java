package org.asaunin.socialnetwork.web.dto;

import lombok.Getter;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.security.SecurityUtils;

import java.util.Date;

@Getter
public class PersonDTO {

	private Long id;
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
	}

}
