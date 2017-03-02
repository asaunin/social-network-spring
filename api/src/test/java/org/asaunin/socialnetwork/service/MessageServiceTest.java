package org.asaunin.socialnetwork.service;

import org.asaunin.socialnetwork.AbstractApplicationTest;
import org.asaunin.socialnetwork.domain.Message;
import org.asaunin.socialnetwork.domain.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MessageServiceTest extends AbstractApplicationTest {

    @Autowired
    private MessageService messageService;

    private final Person person = getDefaultPerson();

    @Test
    public void shouldFindAllDialogMessagesWithPerson() throws Exception {
        final Person interlocutor = Person.builder()
                .id(6L)
                .build();
        final Collection<Message> messages = messageService.getDialog(person, interlocutor);

        assertThat(messages).hasSize(5);
        assertThat(messages)
                .extracting("id", "body")
                .contains(
                        tuple(13L, "Hi geek!"),
                        tuple(15L, "How's old socks?"));
    }

    @Test
    public void shouldFindAllLastMessagesByPerson() throws Exception {
        final Collection<Message> messages = messageService.getLastMessages(person);

        assertThat(messages).hasSize(5);
        assertThat(messages)
                .extracting("id", "body")
                .contains(
                        tuple(19L, "Howdy Antony, long time no seen you!"),
                        tuple(20L, "Buddy, can you add me in your friend list? Thx"));
    }

    @Test
    public void shouldSaveMessage() throws Exception {
        final Collection<Message> before = messageService.getDialog(person, person);

        messageService.send(getDefaultMessage());

        final Collection<Message> after = messageService.getDialog(person, person);

        assertThat(before.size()).isEqualTo(after.size() - 1);
        assertThat(after)
                .extracting("body")
                .contains(DEFAULT_MESSAGE_TEXT);
    }

}
