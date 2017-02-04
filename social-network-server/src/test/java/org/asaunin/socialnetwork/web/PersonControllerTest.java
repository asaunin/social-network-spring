package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.AbstractApplicationTest;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.service.PersonService;
import org.asaunin.socialnetwork.service.PersonService.PersonDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
@EnableSpringDataWebSupport
public class PersonControllerTest extends AbstractApplicationTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private PersonService personService;
	private final Pageable pageRequest = new PageRequest(0, 1);

	private void shouldGetAListInJSonFormat(Page<PersonDTO> peoplePage, String urlTemplate) throws Exception {
		final Person person = getDefaultPerson();
		final List<Person> people = Arrays.asList(person, person);
		final Pageable pageRequest = new PageRequest(0, 1);
		final Page<PersonDTO> value =
				new PageImpl<>(people, pageRequest, people.size())
						.map(e -> new PersonDTO(e, person));

		given(peoplePage).willReturn(value);

		mvc.perform(get(urlTemplate)
				.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalPages").value(2))
				.andExpect(jsonPath("$.content[0].id").value(1L))
				.andExpect(jsonPath("$.content[0].fullName").value("Alex Saunin"));

	}

	@Test
	public void shouldGetAListOfPeopleInJSonFormat() throws Exception {
		shouldGetAListInJSonFormat(
		        personService.getPeople("Alex", pageRequest),
                "/people.json?size=1&searchTerm=Alex");
	}

	@Test
	public void shouldGetAListOfFriendsInJSonFormat() throws Exception {
		shouldGetAListInJSonFormat(
		        personService.getFriends("Alex", pageRequest),
                "/friends.json?size=1&searchTerm=Alex");
	}

	@Test
	public void shouldGetAListOfFollowersInJSonFormat() throws Exception {
		shouldGetAListInJSonFormat(
		        personService.getFriendOf("Alex", pageRequest),
                "/friendOf.json?size=1&searchTerm=Alex");
	}

	@Test
	public void shouldReturnBadRequestWhenPersonMissing() throws Exception {
		final Person person = getDefaultPerson();

		doThrow(new NoSuchElementException()).when(personService).addFriend(person.getId());
		doThrow(new NoSuchElementException()).when(personService).removeFriend(person.getId());

		mvc.perform(put("/friends/add/{personId}.json", person.getId())
				.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());

		mvc.perform(put("/friends/remove/{personId}.json", person.getId())
				.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());

	}

	@Test
	public void shouldAddFriendWhenPersonExists() throws Exception {
		final Person person = getDefaultPerson();

		doNothing().when(personService).addFriend(person.getId());

		mvc.perform(put("/friends/add/{personId}.json", person.getId())
				.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldRemoveAFriendWhenPersonExists() throws Exception {
		final Person person = getDefaultPerson();

		doNothing().when(personService).addFriend(person.getId());

		mvc.perform(put("/friends/remove/{personId}.json", person.getId())
				.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

}
