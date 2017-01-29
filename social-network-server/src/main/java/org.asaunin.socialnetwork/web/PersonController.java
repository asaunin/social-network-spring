package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class PersonController {

	private final PersonService personService;

	@Autowired
	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@GetMapping("/persons")
	public Collection<Person> getPersons() {
		return personService.getPersons();
	}

	@GetMapping("/friends")
	public Collection<Person> getFriends() {
		return personService.getFriends();
	}

	@GetMapping("/followers")
	public Collection<Person> getFollowers() {
		return personService.getFollowers();
	}

}
