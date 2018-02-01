package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.AbstractApplicationTest;
import org.asaunin.socialnetwork.config.Constants;
import org.asaunin.socialnetwork.domain.Message;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.model.MessagePost;
import org.asaunin.socialnetwork.service.MessageService;
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

import java.util.Arrays;

import static org.asaunin.socialnetwork.config.Constants.URI_MESSAGES;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration
@WebMvcTest(value = {MessageController.class, Constants.class})
public class MessageControllerTest extends AbstractApplicationTest {

	private final static String URI = URI_MESSAGES;
	
	private MockMvc mvc;

	@Autowired private WebApplicationContext context;
	@MockBean private MessageService messageService;
	@MockBean private PersonService personService;

	private final Person person = getDefaultPerson();
	private final Message message = getDefaultMessage();

	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.defaultRequest(get("/").with(user(person)))
				.build();
	}

	@Test
	public void getDialogWithExistingPersonShouldReturnListOfMessages() throws Exception {
		given(personService.findById(person.getId())).willReturn(person);
		given(messageService.getDialog(person, person)).willReturn(Arrays.asList(message));

		mvc.perform(
				get(URI + "/dialog/{id}.json", person.getId())
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].body").value(DEFAULT_MESSAGE_TEXT));
	}

	@Test
	public void getDialogWithMissingPersonShouldReturnNotFoundStatus() throws Exception {
		given(personService.findById(Long.MAX_VALUE)).willReturn(null);

		mvc.perform(
				get(URI + "/dialog/{id}.json", Long.MAX_VALUE)
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isNotFound());
	}

	@Test
	public void getLastMessagesShouldReturnListOfMessages() throws Exception {
		given(messageService.getLastMessages(person)).willReturn(Arrays.asList(message));

		mvc.perform(
				get(URI + "/last.json")
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].body").value(DEFAULT_MESSAGE_TEXT));
	}

	@Test
	public void sendMessageShouldReturnCreatedStatus() throws Exception {
		final MessagePost messagePost = getDefaultMessagePost(person);
		given(messageService.send(message)).willReturn(message);

		mvc.perform(
				post(URI + "/add.json")
						.content(convertObjectToJsonBytes(messagePost))
						.contentType(APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated());
	}

}
