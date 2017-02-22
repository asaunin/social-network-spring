package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.security.CurrentProfile;
import org.asaunin.socialnetwork.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.asaunin.socialnetwork.config.Constants.URI_API_PREFIX;

@RestController
@RequestMapping(value = URI_API_PREFIX)
public class ProfileController {

	private final PersonService personService;

	@Autowired
	public ProfileController(PersonService personService) {
		this.personService = personService;
	}

	@GetMapping("/login")
	public PersonService.PersonDTO getPerson(@CurrentProfile Person profile) {
		return personService.getPerson(profile.getId());
	}

}
