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

	public Page<Person> getPersons(Pageable pageRequest) {
		return personRepository.findPersons(pageRequest);
	}

	@Transactional(readOnly = true)
	public Page<Person> getFriends(Pageable pageRequest) {
		return personRepository.findFriends(person, pageRequest);
	}

	@Transactional(readOnly = true)
	public Page<Person> getFollowers(Pageable pageRequest) {
        return personRepository.findFollowers(person, pageRequest);
	}

}
