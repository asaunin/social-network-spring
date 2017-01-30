package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.domain.Message;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.service.MessageService;
import org.asaunin.socialnetwork.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class MessageController {

	private final MessageService messageService;
	private final PersonService personService;

	@Autowired
	public MessageController(MessageService messageService, PersonService personService) {
		this.messageService = messageService;
		this.personService = personService;
	}

	@GetMapping("/messages/{interlocutorId}")
	public Collection<Message> showDialog(@PathVariable("interlocutorId") Long interlocutorId) {
		final Person interlocutor = personService.findById(interlocutorId);
		return messageService.getDialogWithPerson(interlocutor);
	}

	@GetMapping("/messages/last")
	public Collection<Message> showLastMessages() {
		return messageService.getLastMessages();
	}

}
