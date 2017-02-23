package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.config.Constants;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.security.CurrentProfile;
import org.asaunin.socialnetwork.service.PersonService;
import org.asaunin.socialnetwork.web.dto.PersonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = Constants.URI_API_PREFIX)
public class PersonController {

	private static final Logger log = LoggerFactory.getLogger(MessageController.class);

	private final PersonService personService;

	@Autowired
	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@GetMapping("/person/{id}")
	public ResponseEntity<PersonDTO> getPerson(
			@PathVariable("id") Long id) {
		log.warn("REST request to get person id:{}", id);

		final Person person = personService.findById(id);
		if (null == person) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(
				new PersonDTO(person),
				HttpStatus.OK);
	}

	@GetMapping("/people")
	public Page<PersonDTO> getPeople(
			@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
			@PageableDefault(size = 20) Pageable pageRequest) {
		log.warn("REST request to get people list (searchTerm:{}, pageRequest:{})", searchTerm, pageRequest);

		final Page<Person> people = personService.getPeople(searchTerm, pageRequest);

		return people.map(PersonDTO::new);
	}

	@GetMapping("/friends")
	public Page<PersonDTO> getFriends(
			@CurrentProfile Person profile,
			@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
			@PageableDefault(size = 20) Pageable pageRequest) {
		log.warn("REST request to get person's: {} friend list (searchTerm:{}, pageRequest:{})", profile, searchTerm, pageRequest);

		final Page<Person> friends = personService.getFriends(profile, searchTerm, pageRequest);

		return friends.map(PersonDTO::new);
	}

	@GetMapping("/friendOf")
	public Page<PersonDTO> getFriendOf(
			@CurrentProfile Person profile,
			@RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
			@PageableDefault(size = 20) Pageable pageRequest) {
		log.warn("REST request to get person's: {} friend_of list (searchTerm:{}, pageRequest:{})", profile, searchTerm, pageRequest);

		final Page<Person> friendOf = personService.getFriendOf(profile, searchTerm, pageRequest);

		return friendOf.map(PersonDTO::new);
	}

	@PutMapping("/friends/add/{personId}")
	public ResponseEntity addFriend(
			@CurrentProfile Person profile,
			@PathVariable("personId") Long id) {
		log.warn("REST request to add id:{} as a person's: {} friend", id, profile);

		final Person person = personService.findById(id);
		if (null == person) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		personService.addFriend(profile, person);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PutMapping("/friends/remove/{personId}")
	public ResponseEntity removeFriend(
			@CurrentProfile Person profile,
			@PathVariable("personId") Long id) {
		log.warn("REST request to remove id:{} from person: {} friends", id, profile);

		final Person person = personService.findById(id);
		if (null == person) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		personService.removeFriend(profile, person);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	// TODO: 24.02.2017 Move to profile controller
	@PutMapping("/person/update")
	public void updatePerson(@Valid @RequestBody Person person) {
		personService.updatePerson(person);
	}

}
