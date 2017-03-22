package org.asaunin.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.asaunin.socialnetwork.domain.jpa.GenderConverter;
import org.hibernate.annotations.Formula;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "persons")
@NoArgsConstructor
@EqualsAndHashCode(of = {"email"})
@ToString(of = {"id", "fullName"})
public class Person implements UserDetails, Serializable {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	private Long id;

	@Column(length = 50)
	@Getter
	private String firstName;

	@Column(length = 50)
	@Getter
	private String lastName;

	@Column(insertable = false)
	@Getter
	@Formula(value = "concat(first_name, ' ', last_name)")
	private String fullName;

//	@Column(unique = true, length = 50) // TODO: 25.01.2017 Think how provide unique group [shortName + email]
	@Getter @Setter
	private String shortName;

	@Column(unique = true, nullable = false)
	@Getter @Setter
	private String email;

	@Column(length = 60, nullable = false)
	@Getter @Setter
	private String password;

	@Column(length = 15)
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
	@Getter @Setter @JsonIgnore
	private Set<Person> friends = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "friends",
			joinColumns = @JoinColumn(name = "friend_id"),
			inverseJoinColumns = @JoinColumn(name = "person_id"))
	@Getter @Setter @JsonIgnore
	private Set<Person> friendOf = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@Getter @Setter @JsonIgnore
	@JoinTable(name = "user_roles",
			joinColumns = @JoinColumn(name = "person_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	public boolean hasFriend(Person friend) {
		return friends.contains(friend);
	}

	public void addFriend(Person friend) {
		friends.add(friend);
		friend.friendOf.add(this);
	}

	public void removeFriend(Person friend) {
		friends.remove(friend);
		friend.friendOf.remove(this);
	}

	public boolean isFriendOf(Person person) {
		return friendOf.contains(person);
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

	public Person(Long id, String firstName, String lastName, String shortName, String email, String password,
				  String phone, Date birthDate, Gender gender, Set<Role> roles) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.shortName = shortName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.birthDate = birthDate;
		this.gender = gender;
		this.roles = roles;
		setFullName();
	}

	public static PersonBuilder builder() {
		return new PersonBuilder();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getRoles().stream()
				.map(r -> new SimpleGrantedAuthority(r.getName()))
				.collect(Collectors.toSet());
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@ToString(of = {"id", "firstName", "lastName"})
	public static class PersonBuilder {

		private Long id;
		private String firstName = "";
		private String lastName = "";
		private String shortName = "";
		private String email = "";
		private String password = "";
		private String phone = "";
		private Date birthDate;
		private Gender gender = Gender.UNDEFINED;
		private Set<Role> roles = Collections.singleton(Role.USER);

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

		public PersonBuilder password(@NonNull String password) {
			this.password = password;
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

		public PersonBuilder roles(Set<Role> roles) {
			this.roles = roles;
			return this;
		}

		public Person build() {
			return new Person(id, firstName, lastName, shortName, email, password, phone, birthDate, gender, roles);
		}
	}

}
