package org.asaunin.socialnetwork.security;

import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class); // TODO: 15.02.2017 Add logging

    private PersonRepository personRepository;

    public UserDetailsServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Person profile = personRepository.findByEmail(email);
        if (profile == null) {
            throw new UsernameNotFoundException("Profile is not found:" + email);
        }

        log.debug("Profile was found by email @" + email);

        return profile;
    }

}
