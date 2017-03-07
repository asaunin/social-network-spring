package org.asaunin.socialnetwork.service;

import org.asaunin.socialnetwork.AbstractApplicationTest;
import org.asaunin.socialnetwork.domain.Gender;
import org.asaunin.socialnetwork.domain.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PersonServiceTest extends AbstractApplicationTest {

	@Autowired
	private PersonService personService;

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
	public void shouldFindPersonWithCorrectEmail() throws Exception {
		final Person person = personService.findByEmail("alsaunin@gmail.com");

		assertThat(person.getId()).isEqualTo(1L);
		assertThat(person.getEmail()).isEqualTo("alsaunin@gmail.com");
	}

	@Test
	public void shouldFindAllPeople() throws Exception {
		final Page<Person> people = personService.getPeople("", getDefaultPageRequest());

		assertThat(people).hasSize(16);
		assertThat(people)
				.extracting("id", "fullName")
				.contains(
						tuple(1L, "Alex Saunin"),
						tuple(6L, "Michael Corleone"),
						tuple(7L, "Vito Corleone"));
	}

	@Test
	public void shouldFindAllFriends() throws Exception {
		final Person person = personService.findById(1L);
		final Page<Person> friends = personService.getFriends(person, "", getDefaultPageRequest());

		assertThat(friends).hasSize(10);
		assertThat(friends)
				.extracting("id", "fullName")
				.contains(
						tuple(8L, "Tony Soprano"),
						tuple(9L, "Al Capone"));
	}

	@Test
	public void shouldFindAllFriendOf() throws Exception {
		final Person person = personService.findById(1L);
		final Page<Person> friendOf = personService.getFriendOf(person, "", getDefaultPageRequest());

		assertThat(friendOf).hasSize(4);
		assertThat(friendOf)
				.extracting("id", "fullName")
				.contains(
						tuple(16L, "Donnie Brasco"),
						tuple(17L, "Vega Vincent"));
	}

	@Test
	public void shouldFindAPerson() throws Exception {
		final Person person = personService.findById(1L);

		assertThat(person)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("fullName", "Alex Saunin");
	}

	@Test
	public void shouldAddAndRemoveAFriend() throws Exception {
		final Person person = personService.findById(1L);
		final Person friend = personService.findById(15L);

		// Check preconditions
		assertFalse(person.hasFriend(friend));
		assertFalse(person.isFriendOf(friend));
		assertFalse(friend.hasFriend(person));
		assertFalse(friend.isFriendOf(person));

		// Check when person makes friendship with anotherPerson
		personService.addFriend(person, friend);
		assertTrue(person.hasFriend(friend));
		assertFalse(person.isFriendOf(friend));
		assertFalse(friend.hasFriend(person));
		assertTrue(friend.isFriendOf(person));

		// Check when person severs friendship with anotherPerson
		personService.removeFriend(person, friend);
		assertFalse(person.hasFriend(friend));
		assertFalse(person.isFriendOf(friend));
		assertFalse(friend.hasFriend(person));
		assertFalse(friend.isFriendOf(person));
	}

	@Test
	public void shouldUpdatePersonInformation() throws Exception {
		final Person person = personService.findById(1L);
		person.setGender(Gender.UNDEFINED);
		personService.update(person);

		final Person result = personService.findById(person.getId());

		assertThat(result)
				.hasFieldOrPropertyWithValue("id", 1L)
				.hasFieldOrPropertyWithValue("fullName", "Alex Saunin")
				.hasFieldOrPropertyWithValue("gender", Gender.UNDEFINED);
	}

	@Test
	public void shouldChangePassword() throws Exception {
		final Person person = personService.findById(1L);
		final String currentPwd = "12345";
		final String newPwd = "54321";

		assertTrue(personService.hasValidPassword(person, currentPwd));
		assertFalse(personService.hasValidPassword(person, newPwd));

		personService.changePassword(person, newPwd);

		assertFalse(personService.hasValidPassword(person, currentPwd));
		assertTrue(personService.hasValidPassword(person, newPwd));
	}

	@Test
	public void shouldCreateNewPerson() throws Exception {
		final Person actual = personService.create(
				"John",
				"Doe",
				"john.doe@gmail.com",
				"johnny");

		final Person expected = personService.findByEmail("john.doe@gmail.com");

		assertThat(actual)
				.hasFieldOrPropertyWithValue("id", expected.getId())
				.hasFieldOrPropertyWithValue("fullName", expected.getFullName())
				.hasFieldOrPropertyWithValue("email", expected.getEmail());
	}

}
