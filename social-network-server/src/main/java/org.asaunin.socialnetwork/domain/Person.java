package org.asaunin.socialnetwork.domain;

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
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString(of = "fullName")
public class Person {

	@Id @GeneratedValue
	private Long id;

	@NotEmpty
	@Size(min = 3, max = 50)
	@Getter
	private String firstName;

	@NotEmpty
	@Size(min = 3, max = 50)
	@Getter
	private String lastName;

	@Column(insertable = false)
	@Formula(value = "concat(first_name, ' ', last_name)")
	private String fullName;

	@Column(unique = true)
	@Size(min = 5, max = 50)
	@Getter @Setter
	private String shortName;

	@Email
	@Column(unique = true)
	@Getter @Setter
	private String email;

	@Digits(fraction = 0, integer = 15)
	@Getter @Setter
	private String phone;

	@Temporal(TemporalType.DATE)
	@Getter @Setter
	private Date birthDate;

	@Enumerated(EnumType.STRING)
	@Getter @Setter
	private Gender gender = Gender.UNDEFINED;

	@Column(updatable = false, nullable = false)
	@Temporal(TemporalType.DATE)
	@Getter
	private Date created = new Date();

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		setFullName();
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		setFullName();
	}

	private void setFullName() {
		this.fullName = String.format("%s %s", this.firstName, this.lastName);
	}

	public Person(Long id, String firstName, String lastName, String shortName, String email, String phone, Date birthDate, Gender gender) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.shortName = shortName;
		this.email = email;
		this.phone = phone;
		this.birthDate = birthDate;
		this.gender = gender;
		setFullName();
	}

	public static PersonBuilder builder() {
		return new PersonBuilder();
	}

	@ToString
	public static class PersonBuilder {

		private Long id;
		private String firstName;
		private String lastName;
		private String shortName;
		private String email;
		private String phone;
		private Date birthDate;
		private Gender gender = Gender.UNDEFINED;

		PersonBuilder() {
		}

		public PersonBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public PersonBuilder firstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public PersonBuilder lastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public PersonBuilder shortName(String shortName) {
			this.shortName = shortName;
			return this;
		}

		public PersonBuilder email(String email) {
			this.email = email;
			return this;
		}

		public PersonBuilder phone(String phone) {
			this.phone = phone;
			return this;
		}

		public PersonBuilder birthDate(Date birthDate) {
			this.birthDate = birthDate;
			return this;
		}

		public PersonBuilder gender(Gender gender) {
			this.gender = gender;
			return this;
		}

		public Person build() {
			return new Person(id, firstName, lastName, shortName, email, phone, birthDate, gender);
		}

	}

}
