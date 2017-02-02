package org.asaunin.socialnetwork.service;

import org.asaunin.socialnetwork.AbstractApplicationTest;
import org.asaunin.socialnetwork.domain.Gender;
import org.asaunin.socialnetwork.domain.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.transaction.Transactional;
import java.util.GregorianCalendar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceTest extends AbstractApplicationTest {

	@Autowired
	private PersonService personService;

	@Before
	public void setDefaultPerson() {
		ReflectionTestUtils.setField(
				personService,
				"person",
				getDefaultPerson());
	}

	@Test
	public void shouldFindPersonWithCorrectIdAndFields() throws Exception {
		final Person person = personService.findById(1L);

		assertThat(person.getId()).isEqualTo(1L);
		assertThat(person.getFirstName()).isEqualTo("Alex");
		assertThat(person.getLastName()).isEqualTo("Saunin");
		assertThat(person.getShortName()).isEqualTo("maniac");
		assertThat(person.getFullName()).isEqualTo("Alex Saunin");
		assertThat(person.getEmail()).isEqualTo("alsaunin@gmail.com");
		assertThat(person.getPhone()).isEqualTo("79211234567");
		assertThat(person.getBirthDate()).isEqualTo(new GregorianCalendar(1984, 2, 23).getTime());
		assertThat(person.getGender()).isEqualTo(Gender.MALE);
		//...
	}

	@Test
	public void shouldFindPersonWithCorrectShortName() throws Exception {
		final Person person = personService.findByShortName("maniac");

		assertThat(person.getId()).isEqualTo(1L);
		assertThat(person.getShortName()).isEqualTo("maniac");
	}

	@Test
	public void shouldFindAllPersons() throws Exception {
		final Page<Person> persons = personService.getPersons(getDefaultPageRequest());

		assertThat(persons).hasSize(16);
		assertThat(persons)
				.extracting("id", "fullName")
				.contains(
						tuple(1L, "Alex Saunin"),
						tuple(6L, "Michael Corleone"),
						tuple(7L, "Vito Corleone"));
	}

	@Test
	@Transactional
	public void shouldFindAllFriends() throws Exception {
		final Page<Person> persons = personService.getFriends(getDefaultPageRequest());

		assertThat(persons).hasSize(10);
		assertThat(persons)
				.extracting("id", "fullName")
				.contains(
						tuple(8L, "Tony Soprano"),
						tuple(9L, "Al Capone"));
	}

	@Test
	@Transactional
	public void shouldFindAllFollowers() throws Exception {
		final Page<Person> persons = personService.getFollowers(getDefaultPageRequest());

		assertThat(persons).hasSize(4);
		assertThat(persons)
				.extracting("id", "fullName")
				.contains(
						tuple(16L, "Donnie Brasco"),
						tuple(17L, "Vega Vincent"));
	}

}
