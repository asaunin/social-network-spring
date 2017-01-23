package org.asaunin.socialnetwork;

import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.GregorianCalendar;

@SpringBootApplication
public class SocialNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialNetworkApplication.class, args);
    }

    // TODO: 23.01.2017 Replace by data.sql injection
    @Bean
    CommandLineRunner runner(PersonRepository personRepository) {
        return args -> {
            final Person person = Person.builder()
                    .id(1L)
                    .firstName("John")
                    .lastName("Doe")
                    .shortName("johny")
                    .email("johndoe@gmail.com")
                    .birthDate(new GregorianCalendar(1984,3,23).getTime())
                    .phone("792112345678")
                    .build();
            personRepository.save(person);
        };
    }

}

