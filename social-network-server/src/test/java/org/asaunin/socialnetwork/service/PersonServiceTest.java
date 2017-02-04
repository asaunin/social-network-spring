package org.asaunin.socialnetwork.service;

import org.asaunin.socialnetwork.AbstractApplicationTest;
import org.asaunin.socialnetwork.domain.Gender;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.service.PersonService.PersonDTO;
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

	@Before
	public void setDefaultPerson() {
		ReflectionTestUtils.setField(
				personService,
				"person",
				getDefaultPerson());
	}

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
	public void shouldFindAllFriensOf() throws Exception {
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
	public void shouldAddAndRemoveAFriend() throws Exception {
		final Person firstPerson = personService.findById(2L);
		final Person secondPerson = personService.findById(3L);

		// Check preconditions
		assertFalse(firstPerson.hasFriend(secondPerson));
		assertFalse(firstPerson.isFriendOf(secondPerson));
		assertFalse(secondPerson.hasFriend(firstPerson));
		assertFalse(secondPerson.isFriendOf(firstPerson));

		// Check when firstPerson makes friendship with secondPerson
		firstPerson.addFriend(secondPerson);
		assertTrue(firstPerson.hasFriend(secondPerson));
		assertFalse(firstPerson.isFriendOf(secondPerson));
		assertFalse(secondPerson.hasFriend(firstPerson));
		assertTrue(secondPerson.isFriendOf(firstPerson));

		// Check when secondPerson makes friendship with firstPerson
		secondPerson.addFriend(firstPerson);
		assertTrue(firstPerson.hasFriend(secondPerson));
		assertTrue(firstPerson.isFriendOf(secondPerson));
		assertTrue(secondPerson.hasFriend(firstPerson));
		assertTrue(secondPerson.isFriendOf(firstPerson));

		// Check when firstPerson severs friendship with secondPerson
		firstPerson.removeFriend(secondPerson);
		assertFalse(firstPerson.hasFriend(secondPerson));
		assertTrue(firstPerson.isFriendOf(secondPerson));
		assertTrue(secondPerson.hasFriend(firstPerson));
		assertFalse(secondPerson.isFriendOf(firstPerson));

		// Check when secondPerson severs friendship with firstPerson
		secondPerson.removeFriend(firstPerson);
		assertFalse(firstPerson.hasFriend(secondPerson));
		assertFalse(firstPerson.isFriendOf(secondPerson));
		assertFalse(secondPerson.hasFriend(firstPerson));
		assertFalse(secondPerson.isFriendOf(firstPerson));

	}

}
