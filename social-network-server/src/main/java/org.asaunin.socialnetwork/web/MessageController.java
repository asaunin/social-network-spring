package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.domain.Message;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.service.MessageService;
import org.asaunin.socialnetwork.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

	@PostMapping("/messages/add")
	@ResponseStatus(HttpStatus.CREATED)
	public void createMessage(@Valid @RequestBody Message message) {
		messageService.saveMessage(message);
	}

}
