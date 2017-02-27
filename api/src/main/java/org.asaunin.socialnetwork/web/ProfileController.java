package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.model.ProfileContact;
import org.asaunin.socialnetwork.model.PersonView;
import org.asaunin.socialnetwork.security.CurrentProfile;
import org.asaunin.socialnetwork.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.asaunin.socialnetwork.config.Constants.*;

@RestController
@RequestMapping(value = URI_API_PREFIX)
public class ProfileController {

	private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

	private final PersonService personService;

	@Autowired
	public ProfileController(PersonService personService) {
		this.personService = personService;
	}

	@GetMapping("/login")
	public ResponseEntity<PersonView> getCurrent(@CurrentProfile Person profile) {
		log.debug("REST request to get current profile:{}", profile);

		if (null == profile) {
			log.warn("Attempt getting unauthorised profile information failed");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		return ResponseEntity.ok(new PersonView(profile));
	}

	@PutMapping("/updateContact")
	public ResponseEntity<String> updatePerson(
			@CurrentProfile Person profile,
			@Valid @RequestBody ProfileContact contact) {
		log.debug("REST request to update current profile:{} contact information", profile);

		if (!profile.getId().equals(contact.getId())) {
			log.error("Updating profile: {} doesn't match the current one: {}", contact, profile);

			return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(ERROR_UPDATE_PROFILE);
		}

		final String oldEmail = profile.getEmail();
		final String newEmail = contact.getEmail();
		if (!oldEmail.equals(newEmail)) {
			final Person result = personService.findByEmail(newEmail);
			if (null != result) {
				log.debug("Attempt to change email value from: {} to  {} failed! " +
						"Email is already used by another contact : {}", contact, profile);

				return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(ERROR_UPDATE_EMAIL);
			}
		}

		profile.setFirstName(contact.getFirstName());
		profile.setLastName(contact.getLastName());
		profile.setEmail(contact.getEmail());
		profile.setPhone(contact.getPhone());
		profile.setBirthDate(contact.getBirthDate());
		profile.setGender(contact.getGender());
		personService.updatePerson(profile);

		return ResponseEntity.ok().build();
	}

}
