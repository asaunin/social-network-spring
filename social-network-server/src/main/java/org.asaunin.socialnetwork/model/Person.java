package org.asaunin.socialnetwork.model;

import lombok.*;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "persons")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = {"email", "shortName", "firstName", "lastName"})
@ToString(of = "fullName")
public class Person {

	@Id @GeneratedValue
	private Long id;

	@NotEmpty
	@Size(min = 3, max = 50)
	private String firstName;

	@NotEmpty
	@Size(min = 3, max = 50)
	private String lastName;

	@Formula(value = "concat(first_name, ' ', last_name)")
	private String fullName;

	@Column(unique = true)
	@Size(min = 5, max = 50)
	private String shortName;

	@Email
	@Column(unique = true)
	private String email;

	@Digits(fraction = 0, integer = 15)
	private String phone;

	@Temporal(TemporalType.DATE)
	private Date birthDate;

	@Enumerated(EnumType.STRING)
	private Gender gender = Gender.UNDEFINED;

	@Column(updatable = false, nullable = false)
	@Temporal(TemporalType.DATE)
	private Date created = new Date();

	//Otherwise lombook builder fill fields incorrectly
	public static class PersonBuilder {
		private Gender gender = Gender.UNDEFINED;
		private Date created = new Date();
	}

}
