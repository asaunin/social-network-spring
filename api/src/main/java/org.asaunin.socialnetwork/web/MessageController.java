package org.asaunin.socialnetwork.web;

import org.asaunin.socialnetwork.domain.Message;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.security.CurrentProfile;
import org.asaunin.socialnetwork.service.MessageService;
import org.asaunin.socialnetwork.service.PersonService;
import org.asaunin.socialnetwork.web.dto.MessageDTO;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.asaunin.socialnetwork.config.Constants.URI_API_PREFIX;
import static org.asaunin.socialnetwork.config.Constants.URI_MESSAGES;

@RestController
@RequestMapping(value = URI_API_PREFIX + URI_MESSAGES)
public class MessageController {

	private static final Logger log = LoggerFactory.getLogger(MessageController.class);

	private final ModelMapper mapper;
	private final MessageService messageService;
	private final PersonService personService;

	@Autowired
	public MessageController(ModelMapper mapper, MessageService messageService, PersonService personService) {
		this.mapper = mapper;
		this.messageService = messageService;
		this.personService = personService;
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<List<Message>> getDialog(
			@CurrentProfile Person profile,
			@PathVariable("id") Long id) {
		log.warn("REST request to get dialog between id:{} and id:{} persons", profile.getId(), id);

		final Person interlocutor = personService.findById(id);
		if (null == interlocutor) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(
				messageService.getDialog(profile,interlocutor),
				HttpStatus.OK);
}

	@GetMapping(value = "/last")
	public List<Message> getLastMessages(@CurrentProfile Person profile) {
		log.warn("REST request to get profile: {} last messages", profile);

		return messageService.getLastMessages(profile);
	}

	@PostMapping(value = "/add")
	@ResponseStatus(HttpStatus.CREATED)
	public void send(@RequestBody @Valid MessageDTO messageDTO) {
		log.warn("REST request to send message: {}", messageDTO);

		messageService.send(convertFromDTO(messageDTO));
	}

	private Message convertFromDTO(final MessageDTO messageDTO) {
		final Message message = mapper.map(messageDTO, Message.class);
		message.setSender(personService.findById(messageDTO.getSender()));
		message.setRecipient(personService.findById(messageDTO.getRecipient()));

		return message;
	}

	private MessageDTO convertToDTO(final Message message) {
		final MessageDTO messageDTO = mapper.map(message, MessageDTO.class);
		messageDTO.setSender(message.getSender().getId());
		messageDTO.setRecipient(message.getSender().getId());

		return messageDTO;
	}

}
