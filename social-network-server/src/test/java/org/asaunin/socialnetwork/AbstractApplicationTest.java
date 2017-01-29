package org.asaunin.socialnetwork;

import org.asaunin.socialnetwork.domain.Message;
import org.asaunin.socialnetwork.domain.Person;

public abstract class AbstractApplicationTest {

	public final static String DEFAULT_MESSAGE_TEXT = "Lorem ipsum dolor sit amet...";

	protected Person getDefaultPerson() {
		return Person.builder()
				.id(1L)
				.firstName("Alex")
				.lastName("Saunin")
				.shortName("maniac")
				.email("alsaunin@gmail.com")
				.build();
	}

	protected Message getDefaultMessage() {
		final Person person = getDefaultPerson();
		final Message msg = new Message();
		msg.setBody(DEFAULT_MESSAGE_TEXT);
		msg.setSender(person);
		msg.setRecipient(person);
		return msg;
	}

}
