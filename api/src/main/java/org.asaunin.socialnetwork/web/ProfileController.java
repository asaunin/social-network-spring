package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.security.CurrentProfile;
import org.asaunin.socialnetwork.web.dto.PersonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.asaunin.socialnetwork.config.Constants.URI_API_PREFIX;

@RestController
@RequestMapping(value = URI_API_PREFIX)
public class ProfileController {

	private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

	@GetMapping("/login")
	public ResponseEntity<PersonDTO> getCurrent(@CurrentProfile Person profile) {
		log.warn("REST request to get current profile:{}", profile);

		if (null == profile) {
			log.warn("Attempt getting unauthorised profile information failed");
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.build();
		}

		return new ResponseEntity<>(
				new PersonDTO(profile),
				HttpStatus.OK);
	}

}
