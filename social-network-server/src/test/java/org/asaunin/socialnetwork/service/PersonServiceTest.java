package org.asaunin.socialnetwork.service;

import org.asaunin.socialnetwork.domain.Gender;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.repository.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceTest {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private PersonService personService;

	@Before
	public void setupPerson() {
		final Person person = Person.builder()
				.id(1L)
				.firstName("John")
				.lastName("Doe")
				.shortName("johny")
				.email("johndoe@gmail.com")
				.birthDate(new GregorianCalendar(1984,3,23).getTime())
				.phone("792112345678")
				.build();
		personRepository.save(person);
	}

	@Test
	public void shouldFindPersonWithCorrectIdAndFields() throws Exception {
		final Person person = personService.findById(1L);

		assertThat(person).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(person).hasFieldOrPropertyWithValue("firstName", "John");
		assertThat(person).hasFieldOrPropertyWithValue("lastName", "Doe");
		assertThat(person).hasFieldOrPropertyWithValue("shortName", "johny");
		assertThat(person).hasFieldOrPropertyWithValue("fullName", "John Doe");
		assertThat(person).hasFieldOrPropertyWithValue("email", "johndoe@gmail.com");
		assertThat(person).hasFieldOrPropertyWithValue("birthDate", new GregorianCalendar(1984,3,23).getTime());
		assertThat(person).hasFieldOrPropertyWithValue("gender", Gender.UNDEFINED);
		assertThat(person.getCreated()).isNotNull();
		//...
	}

	@Test
	public void shouldFindPersonWithCorrectShortName() throws Exception {
		final Person person = personService.findByShortName("johny");

		assertThat(person).hasFieldOrPropertyWithValue("id", 1L);
		assertThat(person).hasFieldOrPropertyWithValue("shortName", "johny");
	}

	@Test
	public void shouldFindAllPersons() throws Exception {
		final Collection<Person> persons = personService.findAll();

		assertThat(persons).hasSize(1);
	}

}
