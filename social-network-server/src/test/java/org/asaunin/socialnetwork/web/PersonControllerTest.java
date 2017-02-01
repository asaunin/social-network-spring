package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.AbstractApplicationTest;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.service.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest extends AbstractApplicationTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private PersonService personService;

	@Test
	public void shouldGetAListOfPersonsInJSonFormat() throws Exception {
		final Person person = getDefaultPerson();

		given(personService.getPersons()).willReturn(Arrays.asList(person));

		mvc.perform(get("/persons.json")
                .contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].fullName").value("Alex Saunin"));
	}

	@Test
	public void shouldGetAListOfFriendsInJSonFormat() throws Exception {
		final Person person = getDefaultPerson();

		given(personService.getFriends()).willReturn(Arrays.asList(person));

		mvc.perform(get("/friends.json")
                .contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].fullName").value("Alex Saunin"));
	}

	@Test
	public void shouldGetAListOfFollowersInJSonFormat() throws Exception {
		final Person person = getDefaultPerson();

		given(personService.getFollowers()).willReturn(Arrays.asList(person));

		mvc.perform(get("/followers.json")
                .contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(1L))
				.andExpect(jsonPath("$[0].fullName").value("Alex Saunin"));
	}

}
