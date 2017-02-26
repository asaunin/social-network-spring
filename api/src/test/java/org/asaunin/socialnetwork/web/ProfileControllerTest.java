package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.AbstractApplicationTest;
import org.asaunin.socialnetwork.config.Constants;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.model.ProfileContact;
import org.asaunin.socialnetwork.service.PersonService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.asaunin.socialnetwork.config.Constants.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration
@WebMvcTest(value = {ProfileController.class, Constants.class})
public class ProfileControllerTest extends AbstractApplicationTest {

	private MockMvc mvc;

	@Autowired private WebApplicationContext context;
	@MockBean private PersonService personService;

	private final Person person = getDefaultPerson();

	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
//				.apply(springSecurity())
				.build();
	}

	@Test
	public void validLoginAndPasswordAuthenticationShouldReturnUnauthorizedStatus() throws Exception {
		mvc.perform(
				get("/api/login.json")
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void validLoginAndPasswordAuthenticationShouldReturnCurrentProfile() throws Exception {
		mvc.perform(
				get("/api/login.json")
						.with(user(person))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(person.getId()))
				.andExpect(jsonPath("$.fullName").value(person.getFullName()));
	}

	@Test
	@WithMockUser
	public void updateProfileContactInfoWithInvalidIdShouldReturnBadRequestStatus() throws Exception {
		final Person wrongPerson = getWrongPerson();
		final ProfileContact wrongContact = getProfileContact(wrongPerson);

		mvc.perform(
				put("/api/updateContact.json")
						.with(user(person))
						.content(convertObjectToJsonBytes(wrongContact))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest())
				.andExpect(header().string(ERROR_HEADER, ERROR_UPDATE_PROFILE));
	}

	@Test
	@WithMockUser
	public void updateProfileEmailWhichIsAlreadyRegisteredShouldReturnBadRequestStatus() throws Exception {
		final Person wrongPerson = getWrongPerson();
		final ProfileContact contact = getProfileContact(person);
		contact.setEmail(wrongPerson.getEmail());

		given(personService.findByEmail(wrongPerson.getEmail())).willReturn(wrongPerson);

		mvc.perform(
				put("/api/updateContact.json")
						.with(user(person))
						.content(convertObjectToJsonBytes(contact))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest())
				.andExpect(header().string(ERROR_HEADER, ERROR_UPDATE_EMAIL));
	}

	@Test
	public void updateProfileContactInfoWithInvalidDataShouldReturnBadRequestStatus() throws Exception {
		final ProfileContact contact = getProfileContact(person);
		contact.setPhone("");

		doNothing().when(personService).updatePerson(person);

		mvc.perform(
				put("/api/updateContact.json")
						.with(user(person))
						.content(convertObjectToJsonBytes(contact))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser
	public void updateProfileContactInfoWithValidDataShouldReturnOkStatus() throws Exception {
		final ProfileContact contact = getProfileContact(person);

		doNothing().when(personService).updatePerson(person);

		mvc.perform(
				put("/api/updateContact.json")
						.with(user(person))
						.content(convertObjectToJsonBytes(contact))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

	private Person getWrongPerson() {
		return Person.builder()
				.id(8L)
				.firstName("'Tony'")
				.lastName("'Soprano'")
				.email("tony@mail.ru")
				.phone("76545465465")
				.birthDate(new Date())
				.build();
	}


}
