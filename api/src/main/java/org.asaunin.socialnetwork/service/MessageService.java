package org.asaunin.socialnetwork.service;

import org.asaunin.socialnetwork.domain.Message;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

	private MessageRepository messageRepository;

	@Autowired
	public MessageService(MessageRepository messageRepository){
		this.messageRepository = messageRepository;
	}

	public List<Message> getDialog(Person person, Person interlocutor) {
		return messageRepository.findByRecipientOrSenderOrderByPostedDesc(person, interlocutor);
	}

	public List<Message> getLastMessages(Person person) {
		return messageRepository.findLastMessagesByPerson(person);
	}

	public Message send(Message message) {
		return messageRepository.save(message);
	}

}
