package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.AbstractApplicationTest;
import org.asaunin.socialnetwork.config.Constants;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.service.PersonService;
import org.junit.Before;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration
@WebMvcTest(value = {PersonController.class, Constants.class})
@EnableSpringDataWebSupport //For pagination
public class PersonControllerTest extends AbstractApplicationTest {

	private MockMvc mvc;

	@Autowired private WebApplicationContext context;
	@MockBean private PersonService personService;

	private final Person person = getDefaultPerson();
	private final Pageable pageRequest = new PageRequest(0, 1);

	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.defaultRequest(get("/").with(user(person)))
				.build();
	}

	private void getPageablePersonList(Page<Person> peoplePage, String urlTemplate) throws Exception {
		final List<Person> people = Arrays.asList(person, person);
		final Pageable pageRequest = new PageRequest(0, 1);
		final Page<Person> value = new PageImpl<>(people, pageRequest, people.size());

		given(peoplePage).willReturn(value);

		mvc.perform(
				get(urlTemplate)
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalPages").value(2))
				.andExpect(jsonPath("$.content[0].id").value(1L))
				.andExpect(jsonPath("$.content[0].fullName").value("Alex Saunin"));
	}

	@Test
	public void getPeopleShouldReturnPageableListOfPersons() throws Exception {
		getPageablePersonList(
				personService.getPeople("Alex", pageRequest),
				"/api/people.json?size=1&searchTerm=Alex");
	}

	@Test
	public void getFriendsShouldReturnPageableListOfProfileFriends() throws Exception {
		getPageablePersonList(
				personService.getFriends(person, "Alex", pageRequest),
				"/api/friends.json?size=1&searchTerm=Alex");
	}

	@Test
	public void getFriendOfShouldReturnPageableListOfProfileFriendOf() throws Exception {
		getPageablePersonList(
				personService.getFriendOf(person, "Alex", pageRequest),
				"/api/friendOf.json?size=1&searchTerm=Alex");
	}

	@Test
	public void getByExistingIdShouldReturnPerson() throws Exception {
		given(personService.findById(person.getId())).willReturn(person);

		mvc.perform(
				get("/api/person/{personId}.json", person.getId())
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(person.getId()))
				.andExpect(jsonPath("$.fullName").value(person.getFullName()));
	}

	@Test
	public void getByMissingIdShouldReturnNotFoundStatus() throws Exception {
		given(personService.findById(Long.MAX_VALUE)).willReturn(null);

		mvc.perform(
				get("/api/person/{personId}.json", person.getId())
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());
	}

	@Test
	public void addOrRemoveMissingFriendShouldReturnNotFoundStatus() throws Exception {
		given(personService.findById(Long.MAX_VALUE)).willReturn(null);

		mvc.perform(
				put("/api/friends/add/{personId}.json", Long.MAX_VALUE)
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());

		mvc.perform(
				put("/api/friends/remove/{personId}.json", Long.MAX_VALUE)
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());
	}

	@Test
	public void addExistingFriendShouldReturnOkStatus() throws Exception {
		given(personService.findById(person.getId())).willReturn(person);
		doNothing().when(personService).addFriend(person, person);

		mvc.perform(
				put("/api/friends/add/{personId}.json", person.getId())
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

	@Test
	public void removeExistingFriendShouldReturnOkStatus() throws Exception {
		given(personService.findById(person.getId())).willReturn(person);
		doNothing().when(personService).addFriend(person, person);

		mvc.perform(
				put("/api/friends/remove/{personId}.json", person.getId())
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

}
