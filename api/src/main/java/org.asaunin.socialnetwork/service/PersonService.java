package org.asaunin.socialnetwork.service;

import lombok.Getter;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PersonService {

	private final Person person;
	private PersonRepository personRepository;

	@Autowired
	public PersonService(Person person, PersonRepository personRepository) {
		this.person = person;
		this.personRepository = personRepository;
	}

	public Person findById(Long id) {
		final Optional<Person> result = personRepository.findById(id);
		if (result.isPresent()) {
			return result.get();
		}
		throw new NoSuchElementException(String.format("Person with id number %d is not found", id));
	}

	public Person findByShortName(String shortName) {
		return personRepository.findByShortName(shortName);
	}

	@Transactional(readOnly = true)
	public Page<PersonDTO> getPeople(String searchTerm, Pageable pageRequest) {
		final Page<Person> people = personRepository.findPeople(searchTerm, pageRequest);
		return people.map(e -> new PersonDTO(e, person));
	}

	@Transactional(readOnly = true)
	public Page<PersonDTO> getFriends(String searchTerm, Pageable pageRequest) {
		final Page<Person> friends = personRepository.findFriends(person, searchTerm, pageRequest);
		return friends.map(e -> new PersonDTO(e, person));
	}

	@Transactional(readOnly = true)
	public Page<PersonDTO> getFriendOf(String searchTerm, Pageable pageRequest) {
		final Page<Person> friendOf = personRepository.findFriendOf(person, searchTerm, pageRequest);
		return friendOf.map(e -> new PersonDTO(e, person));
	}

	@Transactional
	public PersonDTO getPerson(Long personId) {
		return new PersonDTO(findById(personId), person);
	}

	public Person getAuthorizedPerson() {
		return person;
	}

	@Transactional
	public void addFriend(Long personId) {
		final Person friend = findById(personId);
		if (!person.hasFriend(friend)) {
			person.addFriend(friend);
		}
	}

	@Transactional
	public void removeFriend(Long personId) {
		final Person friend = findById(personId);
		if (person.hasFriend(friend)) {
			person.removeFriend(friend);
		}
	}

	public void updatePerson(Person person) {
		// TODO: 05.02.2017 Better to write individual update query, that not affect the set's
		final Person tmpPerson = findById(person.getId());
		person.setFriends(tmpPerson.getFriends());
		person.setFriendOf(tmpPerson.getFriendOf());
		personRepository.save(person);
	}

	@Getter
	// TODO: 22.02.2017 Move to the DTO package
	public static class PersonDTO {

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

		public PersonDTO(Person entity, Person person) {
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

}
