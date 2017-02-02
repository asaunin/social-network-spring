package org.asaunin.socialnetwork.repository;

import org.asaunin.socialnetwork.domain.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {

	Person findByShortName(String shortName);

	@Query("SELECT p FROM Person p ORDER BY p.fullName")
	Page<Person> findPersons(Pageable pageRequest);

	@Query("SELECT p FROM Person p WHERE (:person) MEMBER OF p.followers ORDER BY p.fullName")
	Page<Person> findFriends(@Param("person") Person person, Pageable pageRequest);

	@Query("SELECT p FROM Person p WHERE (:person) MEMBER OF p.friends ORDER BY p.fullName")
	Page<Person> findFollowers(@Param("person") Person person, Pageable pageRequest);

}
