package org.asaunin.socialnetwork;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asaunin.socialnetwork.domain.Message;
import org.asaunin.socialnetwork.domain.Person;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;

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

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

}
