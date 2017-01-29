package org.asaunin.socialnetwork.service;

import org.asaunin.socialnetwork.domain.Message;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MessageService {

	private final Person person;
	private MessageRepository messageRepository;

	@Autowired
	public MessageService(Person person, MessageRepository messageRepository){
		this.person = person;
		this.messageRepository = messageRepository;
	}

	public Collection<Message> getDialogWithPerson(Person interlocutor) {
		return messageRepository.findByRecipientOrSenderOrderByPostedDesc(person, interlocutor);
	}

}
