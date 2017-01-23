package org.asaunin.socialnetwork.repository;

import org.asaunin.socialnetwork.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface PersonRepository extends CrudRepository<Person, Long> {

	Person findByShortName(String shortName);

	Collection<Person> findAll();

}
