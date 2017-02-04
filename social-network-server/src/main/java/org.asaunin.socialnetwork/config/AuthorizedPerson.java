package org.asaunin.socialnetwork.config;

import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.repository.PersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;

// Temporary bean while authentication is not yet implemented
@Configuration
@Transactional
public class AuthorizedPerson {

	@Bean
	public Person person(PersonRepository personRepository) {
		final Person person = personRepository.findOne(1L);
		person.getFriends().isEmpty();
		person.getFriendOf().isEmpty();
		return person;
	}

}
