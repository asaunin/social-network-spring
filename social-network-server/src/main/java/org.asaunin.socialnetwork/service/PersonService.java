package org.asaunin.socialnetwork.service;

import org.asaunin.socialnetwork.model.Person;
import org.asaunin.socialnetwork.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PersonService {

	private PersonRepository personRepository;

	@Autowired
	public PersonService(PersonRepository personRepository){
		this.personRepository = personRepository;
	}

	public Person findById(Long id) {
		return personRepository.findOne(id);
	}

	public Person findByShortName(String shortName) {
		return personRepository.findByShortName(shortName);
	}

	public Collection<Person> findAll() {
		return personRepository.findAll();
	}

}
