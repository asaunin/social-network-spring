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

	public Page<Person> getPeople(String searchTerm, Pageable pageRequest) {
		return personRepository.findPeople(searchTerm, pageRequest);
	}

	@Transactional(readOnly = true)
	public Page<Person> getFriends(String searchTerm, Pageable pageRequest) {
		return personRepository.findFriends(person, searchTerm, pageRequest);
	}

	@Transactional(readOnly = true)
	public Page<Person> getFollowers(String searchTerm, Pageable pageRequest) {
        return personRepository.findFollowers(person, searchTerm, pageRequest);
	}

}
