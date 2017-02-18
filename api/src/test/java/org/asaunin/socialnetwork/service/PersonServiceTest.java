package org.asaunin.socialnetwork.service;

import org.asaunin.socialnetwork.AbstractApplicationTest;
import org.asaunin.socialnetwork.domain.Gender;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.service.PersonService.PersonDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonServiceTest extends AbstractApplicationTest {

	@Autowired
	private PersonService personService;

	@Test(expected = NoSuchElementException.class)
	public void shouldReturnAnExceptionWhenThereIsNoPerson() throws Exception {
		final Person person = personService.findById(Long.MAX_VALUE);
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
	@Transactional
	public void shouldFindAllPeople() throws Exception {
		final Page<PersonDTO> people = personService.getPeople("", getDefaultPageRequest());

		assertThat(people).hasSize(16);
		assertThat(people)
				.extracting("id", "fullName")
				.contains(
						tuple(1L, "Alex Saunin"),
						tuple(6L, "Michael Corleone"),
						tuple(7L, "Vito Corleone"));
	}

	@Test
	@Transactional
	public void shouldFindAllFriends() throws Exception {
		final Page<PersonDTO> friends = personService.getFriends("", getDefaultPageRequest());

		assertThat(friends).hasSize(10);
		assertThat(friends)
				.extracting("id", "fullName")
				.contains(
						tuple(8L, "Tony Soprano"),
						tuple(9L, "Al Capone"));
	}

	@Test
	@Transactional
	public void shouldFindAllFriendOf() throws Exception {
		final Page<PersonDTO> friendOf = personService.getFriendOf("", getDefaultPageRequest());

		assertThat(friendOf).hasSize(4);
		assertThat(friendOf)
				.extracting("id", "fullName")
				.contains(
						tuple(16L, "Donnie Brasco"),
						tuple(17L, "Vega Vincent"));
	}

	@Test
	@Transactional
	public void shouldFindAPerson() throws Exception {
		final PersonDTO person = personService.getPerson(1L);

		assertThat(person)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("fullName", "Alex Saunin");
	}

	@Test
	@Transactional
	public void shouldUpdatePersonInformation() throws Exception {
		final Person person = personService.getAuthorizedPerson();
		person.setGender(Gender.UNDEFINED);
		personService.updatePerson(person);

		final Person result = personService.findById(person.getId());

		assertThat(result)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("fullName", "Alex Saunin")
				.hasFieldOrPropertyWithValue("gender", Gender.UNDEFINED);
	}

	@Test
	@Transactional
	public void shouldAddAndRemoveAFriend() throws Exception {
		final Person person = personService.getAuthorizedPerson();
		final Person anotherPerson = personService.findById(15L);

		// Check preconditions
		assertFalse(person.hasFriend(anotherPerson));
		assertFalse(person.isFriendOf(anotherPerson));
		assertFalse(anotherPerson.hasFriend(person));
		assertFalse(anotherPerson.isFriendOf(person));

		// Check when person makes friendship with anotherPerson
		personService.addFriend(anotherPerson.getId());
		assertTrue(person.hasFriend(anotherPerson));
		assertFalse(person.isFriendOf(anotherPerson));
		assertFalse(anotherPerson.hasFriend(person));
		assertTrue(anotherPerson.isFriendOf(person));

		// Check when person severs friendship with anotherPerson
		personService.removeFriend(anotherPerson.getId());
		assertFalse(person.hasFriend(anotherPerson));
		assertFalse(person.isFriendOf(anotherPerson));
		assertFalse(anotherPerson.hasFriend(person));
		assertFalse(anotherPerson.isFriendOf(person));
	}

}
