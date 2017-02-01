package org.asaunin.socialnetwork.config;

import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.repository.PersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Temporary bean while authentication is not yet implemented
@Configuration
public class DefaultPerson {

	@Bean
	Person person(PersonRepository personRepository) {
		return personRepository.findOne(1L);
	}

}
