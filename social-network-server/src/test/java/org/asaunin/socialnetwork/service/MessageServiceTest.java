package org.asaunin.socialnetwork.service;

import org.asaunin.socialnetwork.AbstractApplicationTest;
import org.asaunin.socialnetwork.domain.Message;
import org.asaunin.socialnetwork.domain.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.transaction.Transactional;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest extends AbstractApplicationTest {

	@Autowired
	private MessageService messageService;

	@Before
	public void setDefaultPerson() {
		ReflectionTestUtils.setField(
				messageService,
				"person",
				getDefaultPerson());
	}

	@Test
	@Transactional
	public void shouldFindAllDialogMessagesWithPerson() throws Exception {
		final Person interlocutor = Person.builder()
				.id(6L)
				.build();
		final Collection<Message> persons = messageService.getDialogWithPerson(interlocutor);

		assertThat(persons).hasSize(5);
		assertThat(persons)
				.extracting("id", "body")
				.contains(
						tuple(13L, "Hi geek!"),
						tuple(15L, "How's old socks?"));
	}

}
