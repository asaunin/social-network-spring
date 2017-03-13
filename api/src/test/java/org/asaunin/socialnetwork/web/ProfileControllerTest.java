package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.AbstractApplicationTest;
import org.asaunin.socialnetwork.config.Constants;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.model.ChangePassword;
import org.asaunin.socialnetwork.model.ContactInformation;
import org.asaunin.socialnetwork.model.SignUp;
import org.asaunin.socialnetwork.service.PersonService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
	public void updateContactInformationWithInvalidIdShouldReturnBadRequestStatus() throws Exception {
		final Person wrongPerson = getSignedUpPerson();
		final ContactInformation wrongContact = getContactInformation(wrongPerson);

		mvc.perform(
				put("/api/updateContact.json")
						.with(user(person))
						.content(convertObjectToJsonBytes(wrongContact))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void updateContactInformationWithInvalidDataShouldReturnBadRequestStatus() throws Exception {
		final ContactInformation contact = getContactInformation(person);
		contact.setPhone("");

		mvc.perform(
				put("/api/updateContact.json")
						.with(user(person))
						.content(convertObjectToJsonBytes(contact))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void updateProfileWithNonUniqueEmailShouldReturnBadRequestStatus() throws Exception {
		final Person signedUpPerson = getSignedUpPerson();
		final ContactInformation contact = getContactInformation(person);
		contact.setEmail(signedUpPerson.getEmail());

		given(personService.findByEmail(signedUpPerson.getEmail())).willReturn(signedUpPerson);

		mvc.perform(
				put("/api/updateContact.json")
						.with(user(person))
						.content(convertObjectToJsonBytes(contact))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(ERROR_UPDATE_EMAIL));
	}

	@Test
	public void updateContactInformationWithValidDataShouldReturnOkStatus() throws Exception {
		final ContactInformation contact = getContactInformation(person);

		doNothing().when(personService).update(person);

		mvc.perform(
				put("/api/updateContact.json")
						.with(user(person))
						.content(convertObjectToJsonBytes(contact))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

	@Test
	public void signUpWithInvalidDataShouldReturnBadRequestStatus() throws Exception {
		final SignUp signUp = getSignUp(person);
		signUp.setPassword("-");

		mvc.perform(
				post("/api/signUp.json")
						.with(user(person))
						.content(convertObjectToJsonBytes(signUp))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void signUpWithNonUniqueEmailShouldReturnBadRequestStatus() throws Exception {
		final Person newPerson = getNewPerson();
		final Person signedUpPerson = getSignedUpPerson();
		final SignUp signUp = getSignUp(newPerson);
		signUp.setUserName(signedUpPerson.getEmail());

		given(personService.findByEmail(signedUpPerson.getEmail())).willReturn(signedUpPerson);

		mvc.perform(
				post("/api/signUp.json")
						.content(convertObjectToJsonBytes(signUp))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(ERROR_SIGN_UP_EMAIL));
	}

	@Test
	public void signUpWithValidDataShouldReturnOkStatus() throws Exception {
		final Person newPerson = getNewPerson();
		final SignUp signUp = getSignUp(newPerson);

		given(personService.findByEmail(newPerson.getEmail())).willReturn(null);
		given(personService.create(
				newPerson.getFirstName(),
				newPerson.getLastName(),
				newPerson.getEmail(),
				newPerson.getPassword()))
				.willReturn(newPerson);

		mvc.perform(
				post("/api/signUp.json")
						.content(convertObjectToJsonBytes(signUp))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated());
	}

	@Test
	public void changePasswordWithInvalidDataShouldReturnBadRequestStatus() throws Exception {
		final ChangePassword pwd = new ChangePassword("12345", "12");

		mvc.perform(
				post("/api/changePassword.json")
						.with(user(person))
						.content(convertObjectToJsonBytes(pwd))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void changePasswordWithInvalidCurrentOneShouldReturnBadRequestStatus() throws Exception {
		final ChangePassword pwd = new ChangePassword("54321", "11111");

		given(personService.hasValidPassword(person, pwd.getCurrentPassword())).willReturn(false);

		mvc.perform(
				post("/api/changePassword.json")
						.with(user(person))
						.content(convertObjectToJsonBytes(pwd))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(ERROR_PASSWORD_CONFIRMATION));
	}

	@Test
	public void changePasswordWithValidDataShouldReturnOkStatus() throws Exception {
		final ChangePassword pwd = new ChangePassword("12345", "54321");

		given(personService.hasValidPassword(person, pwd.getCurrentPassword())).willReturn(true);
		doNothing().when(personService).changePassword(person, pwd.getPassword());

		mvc.perform(
				post("/api/changePassword.json")
						.with(user(person))
						.content(convertObjectToJsonBytes(pwd))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk());
	}

	private static Person getSignedUpPerson() {
		return Person.builder()
				.id(8L)
				.firstName("Tony")
				.lastName("Soprano")
				.email("tony@mail.ru")
				.phone("76545465465")
				.birthDate(new Date())
				.build();
	}

	private static Person getNewPerson() {
		return Person.builder()
				.firstName("John")
				.lastName("Doe")
				.email("john.doe@gmail.com")
				.password("johnny")
				.build();
	}

	private static ContactInformation getContactInformation(Person person) {
		return new ContactInformation(
				person.getId(),
				person.getFirstName(),
				person.getLastName(),
				person.getEmail(),
				person.getPhone(),
				person.getBirthDate(),
				person.getGender());
	}

	private static SignUp getSignUp(Person person) {
		return new SignUp(
				person.getFirstName(),
				person.getLastName(),
				person.getEmail(),
				person.getPassword());
	}

}
