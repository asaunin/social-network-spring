package org.asaunin.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.asaunin.socialnetwork.domain.jpa.GenderConverter;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "persons")
@NoArgsConstructor
@EqualsAndHashCode(of = {"email", "created"})
@ToString(of = {"id", "fullName"})
public class Person {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
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
	@Getter
	private String fullName;

	@Column(unique = true) // TODO: 25.01.2017 Think how to solve initialisation problem
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

	@Convert(converter = GenderConverter.class)
	@Getter @Setter
	private Gender gender = Gender.UNDEFINED;

	@Column(updatable = false, nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Getter
	private Date created = new Date();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "friends",
			joinColumns = @JoinColumn(name = "person_id"),
			inverseJoinColumns = @JoinColumn(name = "friend_id"))
	@Getter @JsonIgnore
	private Set<Person> friends;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "friends",
			joinColumns = @JoinColumn(name = "friend_id"),
			inverseJoinColumns = @JoinColumn(name = "person_id"))
	@Getter @JsonIgnore
	private Set<Person> followers;

	public boolean isFriend(Person friend) {
		return friends.contains(friend);
	}

	public boolean isFollower(Person follower) {
		return followers.contains(follower);
	}

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

		public PersonBuilder id(@NonNull Long id) {
			this.id = id;
			return this;
		}

		public PersonBuilder firstName(@NonNull String firstName) {
			this.firstName = firstName;
			return this;
		}

		public PersonBuilder lastName(@NonNull String lastName) {
			this.lastName = lastName;
			return this;
		}

		public PersonBuilder shortName(String shortName) {
			this.shortName = shortName;
			return this;
		}

		public PersonBuilder email(@NonNull String email) {
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
