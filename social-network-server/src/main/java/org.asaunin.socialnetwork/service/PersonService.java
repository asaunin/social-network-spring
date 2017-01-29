package org.asaunin.socialnetwork.service;

import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
public class PersonService {

	private final Person person;
	private PersonRepository personRepository;

	@Autowired
	public PersonService(Person person, PersonRepository personRepository){
		this.person = person;
		this.personRepository = personRepository;
	}

	public Person findById(Long id) {
		return personRepository.findOne(id);
	}

	public Person findByShortName(String shortName) {
		return personRepository.findByShortName(shortName);
	}

	public Collection<Person> getPersons() {
		return personRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Collection<Person> getFriends() {
		return personRepository
				.findOne(person.getId())
				.getFriends();
	}

	@Transactional(readOnly = true)
	public Collection<Person> getFollowers() {
		return personRepository
				.findOne(person.getId())
				.getFollowers();
	}

}
