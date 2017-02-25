package org.asaunin.socialnetwork;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asaunin.socialnetwork.domain.Message;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.web.dto.MessagePost;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;

public abstract class AbstractApplicationTest {

    protected final static String DEFAULT_MESSAGE_TEXT = "Lorem ipsum dolor sit amet...";

    protected static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    protected Person getDefaultPerson() {
        return Person.builder()
                .id(1L)
                .firstName("Alex")
                .lastName("Saunin")
                .shortName("maniac")
                .email("alsaunin@gmail.com")
                .password("123")
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

    protected MessagePost getDefaultMessageDTO() {
        final Person person = getDefaultPerson();
        final MessagePost msg = new MessagePost();
        msg.setBody(DEFAULT_MESSAGE_TEXT);
        msg.setSender(person.getId());
        msg.setRecipient(person.getId());
        return msg;
    }

    protected Pageable getDefaultPageRequest() {
	    return new PageRequest(0, 20);
//	    return new PageRequest(1,
//			    10,
//			    new Sort(Sort.Direction.DESC, "description")
//					    .and(new Sort(Sort.Direction.ASC, "title")));
    }

    protected static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

}
