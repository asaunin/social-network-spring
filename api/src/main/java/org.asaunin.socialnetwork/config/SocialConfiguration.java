package org.asaunin.socialnetwork.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.asaunin.socialnetwork.service.SocialConnectionSignUp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Configuration
@EnableSocial
@RequiredArgsConstructor
@ConfigurationProperties("spring.social")
public class SocialConfiguration implements SocialConfigurer {

    private static final Logger log = LoggerFactory.getLogger(SocialConfiguration.class);

    private final SocialConnectionSignUp connectionSignUp;

    @Getter @Setter
    @NestedConfigurationProperty
    private SocialConnection facebook;

    @Getter @Setter
    @NestedConfigurationProperty
    private SocialConnection google;

    @Getter
    @Setter
    public static class SocialConnection {

        private String clientId;
        private String clientSecret;

    }

    // TODO: 01.02.2018 Figure out how to fix FB API bio error with more elegant way
    @PostConstruct
    private void init() {
        try {
            final String[] fieldsToMap = {
                    "id", "about", "age_range", "birthday", "context", "cover", "currency", "devices", "education",
                    "email", "favorite_athletes", "favorite_teams", "first_name", "gender", "hometown",
                    "inspirational_people", "installed", "install_type", "is_verified", "languages", "last_name",
                    "link", "locale", "location", "meeting_for", "middle_name", "name", "name_format", "political",
                    "quotes", "payment_pricepoints", "relationship_status", "religion", "security_settings",
                    "significant_other", "sports", "test_group", "timezone", "third_party_id", "updated_time",
                    "verified", "viewer_can_send_gift", "website", "work"
            };

            final Field field = Class.forName("org.springframework.social.facebook.api.UserOperations").
                    getDeclaredField("PROFILE_FIELDS");
            field.setAccessible(true);

            final Field modifiers = field.getClass().getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, fieldsToMap);

        } catch (Exception ex) {
            log.error("Failed to define facebook profile fields");
        }
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        // Facebook configuration
        final String facebookClientId = facebook.getClientId();
        final String facebookClientSecret = facebook.getClientSecret();
        if (!StringUtils.isEmpty(facebookClientId) && !StringUtils.isEmpty(facebookClientSecret)) {
            log.debug("Configuring FacebookConnectionFactory...");
            final FacebookConnectionFactory facebookConnectionFactory =
                    new FacebookConnectionFactory(facebookClientId, facebookClientSecret);
            facebookConnectionFactory.setScope("public_profile, email");
            connectionFactoryConfigurer.addConnectionFactory(facebookConnectionFactory);
        } else {
            log.warn("Cannot configure FacebookConnectionFactory id or secret is null");
        }

        // Google configuration
            final String googleClientId = google.getClientId();
            final String googleClientSecret = google.getClientSecret();
        if (!StringUtils.isEmpty(googleClientId) && !StringUtils.isEmpty(googleClientSecret)) {
            log.debug("Configuring GoogleConnectionFactory");
            final GoogleConnectionFactory googleConnectionFactory =
                    new GoogleConnectionFactory(googleClientId, googleClientSecret);
            googleConnectionFactory.setScope("https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email");
            connectionFactoryConfigurer.addConnectionFactory(
                    googleConnectionFactory);
        } else {
            log.warn("Cannot configure GoogleConnectionFactory id or secret null");
        }
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        final InMemoryUsersConnectionRepository inMemoryUsersConnectionRepository =
                new InMemoryUsersConnectionRepository(connectionFactoryLocator);
        inMemoryUsersConnectionRepository.setConnectionSignUp(connectionSignUp);
        return inMemoryUsersConnectionRepository;
    }

}
