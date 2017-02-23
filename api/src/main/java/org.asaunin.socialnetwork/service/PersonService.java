package org.asaunin.socialnetwork.service;

import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

	private PersonRepository personRepository;

	@Autowired
	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@Transactional(readOnly = true)
	public Person findById(Long id) {
		return personRepository.findOne(id);
	}

	@Transactional(readOnly = true)
	public Person findByShortName(String shortName) {
		return personRepository.findByShortName(shortName);
	}

	@Transactional(readOnly = true)
	public Page<Person> getPeople(String searchTerm, Pageable pageRequest) {
		return personRepository.findPeople(searchTerm, pageRequest);
	}

	@Transactional(readOnly = true)
	public Page<Person> getFriends(Person person, String searchTerm, Pageable pageRequest) {
		return personRepository.findFriends(person, searchTerm, pageRequest);
	}

	@Transactional(readOnly = true)
	public Page<Person> getFriendOf(Person person, String searchTerm, Pageable pageRequest) {
		return personRepository.findFriendOf(person, searchTerm, pageRequest);
	}

	@Transactional
	public void addFriend(Person person, Person friend) {
		if (!person.hasFriend(friend)) {
			person.addFriend(friend);
		}
	}

	@Transactional
	public void removeFriend(Person person, Person friend) {
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

}
