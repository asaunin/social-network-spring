package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController {

	private final PersonService personService;

	@Autowired
	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@GetMapping("/people")
	public Page<Person> getPersons(
			@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
			@PageableDefault(size = 20) Pageable pageRequest) {
		return personService.getPeople(searchTerm, pageRequest);
	}

	@GetMapping("/friends")
	public Page<Person> getFriends(
			@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
			@PageableDefault(size = 20) Pageable pageRequest) {
		return personService.getFriends(searchTerm, pageRequest);
	}

	@GetMapping("/followers")
	public Page<Person> getFollowers(
			@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
			@PageableDefault(size = 20) Pageable pageRequest) {
		return personService.getFollowers(searchTerm, pageRequest);
	}

}
