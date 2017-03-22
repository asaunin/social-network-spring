package org.asaunin.socialnetwork.security;

import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private PersonRepository personRepository;

    public UserDetailsServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Authenticating request: {}", email);

        // TODO: 22.02.2017 Provide authentication both by login & email
        final Person profile = personRepository.findByEmail(email);
        if (profile == null) {
            throw new UsernameNotFoundException("Profile is not found:" + email);
        }

        log.info("Profile was found by e-mail: {}", email);

        profile.getFriends().isEmpty();
        profile.getFriendOf().isEmpty();
        profile.getRoles().isEmpty();

        return profile;
    }

}
