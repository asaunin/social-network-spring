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

import static org.asaunin.socialnetwork.config.Constants.URI_API_PREFIX;
import static org.asaunin.socialnetwork.config.Constants.URI_MESSAGES;

@RestController
@RequestMapping(value = URI_API_PREFIX + URI_MESSAGES)
public class MessageController {

	private final MessageService messageService;
	private final PersonService personService;

	@Autowired
	public MessageController(MessageService messageService, PersonService personService) {
		this.messageService = messageService;
		this.personService = personService;
	}

	@GetMapping(value = "/{interlocutorId}")
	public Collection<Message> showDialog(@PathVariable("interlocutorId") Long interlocutorId) {
		final Person interlocutor = personService.findById(interlocutorId);
		return messageService.getDialogWithPerson(interlocutor);
	}

	@GetMapping(value = "/last")
	public Collection<Message> showLastMessages() {
		return messageService.getLastMessages();
	}

	@PostMapping(value = "/add")
	@ResponseStatus(HttpStatus.CREATED)
	public void createMessage(@Valid @RequestBody Message message) {
		messageService.postMessage(message);
	}

}
