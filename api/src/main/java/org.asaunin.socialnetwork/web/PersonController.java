package org.asaunin.socialnetwork.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.asaunin.socialnetwork.domain.Person;
import org.asaunin.socialnetwork.model.PersonView;
import org.asaunin.socialnetwork.security.CurrentProfile;
import org.asaunin.socialnetwork.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static org.asaunin.socialnetwork.config.Constants.URI_API_PREFIX;

@Api(tags = "Person", description = "Operations about persons")
@RestController
@RequestMapping(value = URI_API_PREFIX, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    private final PersonService personService;

    @ApiOperation(value = "Find a person by Id")
    @GetMapping("/person/{id}")
    public ResponseEntity<PersonView> getPerson(
            @PathVariable("id") Long id) {
        log.debug("REST request to get person id:{}", id);

        final Person person = personService.findById(id);
        if (null == person) {
            log.debug("Person id:{} is not signed up", id);

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new PersonView(person));
    }

    @ApiOperation(value = "Persons search")
    @GetMapping("/people")
    public Page<PersonView> getPeople(
            @RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
            @PageableDefault(size = 20) Pageable pageRequest) {
        log.debug("REST request to get people list (searchTerm:{}, pageRequest:{})", searchTerm, pageRequest);

        final Page<Person> people = personService.getPeople(searchTerm, pageRequest);

        return people.map(PersonView::new);
    }

    @ApiOperation(value = "Find friends")
    @GetMapping("/friends")
    public Page<PersonView> getFriends(
            @ApiIgnore @CurrentProfile Person profile,
            @RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
            @PageableDefault(size = 20) Pageable pageRequest) {
        log.debug("REST request to get person's: {} friend list (searchTerm:{}, pageRequest:{})", profile, searchTerm, pageRequest);

        final Page<Person> friends = personService.getFriends(profile, searchTerm, pageRequest);

        return friends.map(PersonView::new);
    }

    @ApiOperation(value = "Find followers")
    @GetMapping("/friendOf")
    public Page<PersonView> getFriendOf(
            @ApiIgnore @CurrentProfile Person profile,
            @RequestParam(name = "searchTerm", defaultValue = "", required = false) String searchTerm,
            @PageableDefault(size = 20) Pageable pageRequest) {
        log.debug("REST request to get person's: {} friend_of list (searchTerm:{}, pageRequest:{})", profile, searchTerm, pageRequest);

        final Page<Person> friendOf = personService.getFriendOf(profile, searchTerm, pageRequest);

        return friendOf.map(PersonView::new);
    }

    @ApiOperation(value = "Add as friend")
    @PutMapping("/friends/add/{personId}")
    public ResponseEntity<Void> addFriend(
            @ApiIgnore @CurrentProfile Person profile,
            @PathVariable("personId") Long id) {
        log.debug("REST request to add id:{} as a person's: {} friend", id, profile);

        final Person person = personService.findById(id);
        if (null == person) {
            return ResponseEntity.notFound().build();
        }

        personService.addFriend(profile, person);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Remove friend")
    @PutMapping("/friends/remove/{personId}")
    public ResponseEntity<Void> removeFriend(
            @ApiIgnore @CurrentProfile Person profile,
            @PathVariable("personId") Long id) {
        log.debug("REST request to remove id:{} from person: {} friends", id, profile);

        final Person person = personService.findById(id);
        if (null == person) {
            return ResponseEntity.notFound().build();
        }

        personService.removeFriend(profile, person);

        return ResponseEntity.ok().build();
    }
}
