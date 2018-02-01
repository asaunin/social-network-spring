package org.asaunin.socialnetwork.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.asaunin.socialnetwork.domain.Message;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.model.MessagePost;
import org.asaunin.socialnetwork.model.MessageView;
import org.asaunin.socialnetwork.security.CurrentProfile;
import org.asaunin.socialnetwork.service.MessageService;
import org.asaunin.socialnetwork.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.asaunin.socialnetwork.config.Constants.URI_MESSAGES;

@Api(tags = "Message", description = "Messaging operations")
@RestController
@RequestMapping(value = URI_MESSAGES, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    private final MessageService messageService;
    private final PersonService personService;

    @ApiOperation(value = "Dialog with a person")
    @GetMapping(value = "/dialog/{id}")
    public ResponseEntity<List<MessageView>> getDialog(
            @ApiIgnore @CurrentProfile Person profile,
            @PathVariable("id") Long id) {
        log.debug("REST request to get dialog between id:{} and id:{} persons", profile.getId(), id);

        final Person interlocutor = personService.findById(id);
        if (null == interlocutor) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(map(messageService.getDialog(profile, interlocutor)));
    }

    @ApiOperation(value = "Resent posts")
    @GetMapping(value = "/last")
    public List<MessageView> getLastMessages(@ApiIgnore @CurrentProfile Person profile) {
        log.debug("REST request to get profile: {} last messages", profile);

        return map(messageService.getLastMessages(profile));
    }

    @ApiOperation(value = "Send new message")
    @PostMapping(value = "/add")
    @ResponseStatus(HttpStatus.CREATED)
    public void send(@RequestBody @Valid MessagePost messagePost) {
        log.debug("REST request to send message: {}", messagePost);

        final Message message = new Message();
        message.setBody(messagePost.getBody());
        message.setSender(personService.findById(messagePost.getSender()));
        message.setRecipient(personService.findById(messagePost.getRecipient()));

        messageService.send(message);
    }

    private List<MessageView> map(List<Message> messages) {
        return messages.stream()
                .map(MessageView::new)
                .collect(toList());
    }

}
