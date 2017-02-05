package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.NoSuchElementException;

import static org.asaunin.socialnetwork.service.PersonService.PersonDTO;

@RestController
public class PersonController {

	private final PersonService personService;

	@Autowired
	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@GetMapping("/person/{personId}")
	public PersonDTO getPerson(@PathVariable("personId") Long personId) {
		return personService.getPerson(personId);
	}

	@PutMapping("/person/update")
	public void updatePerson(@Valid @RequestBody Person person) {
		personService.updatePerson(person);
	}

	@GetMapping("/people")
	public Page<PersonDTO> getPersons(
			@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
			@PageableDefault(size = 20) Pageable pageRequest) {
		return personService.getPeople(searchTerm, pageRequest);
	}

	@GetMapping("/friends")
	public Page<PersonDTO> getFriends(
			@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
			@PageableDefault(size = 20) Pageable pageRequest) {
		return personService.getFriends(searchTerm, pageRequest);
	}

	@GetMapping("/friendOf")
	public Page<PersonDTO> getFriendOf(
			@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
			@PageableDefault(size = 20) Pageable pageRequest) {
		return personService.getFriendOf(searchTerm, pageRequest);
	}

	@PutMapping("/friends/add/{personId}")
	public void addFriend(@PathVariable("personId") Long personId) {
		personService.addFriend(personId);
	}

	@PutMapping("/friends/remove/{personId}")
	public void removeFriend(@PathVariable("personId") Long personId) {
		personService.removeFriend(personId);
	}

	// TODO: 04.02.2017 Replace by the global handler
	@ExceptionHandler(NoSuchElementException.class)
	public void handlePersonNotFound(NoSuchElementException exception, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
	}

}
