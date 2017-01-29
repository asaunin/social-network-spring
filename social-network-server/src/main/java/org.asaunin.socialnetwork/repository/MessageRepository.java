package org.asaunin.socialnetwork.repository;

import org.asaunin.socialnetwork.domain.Message;
import org.asaunin.socialnetwork.domain.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface MessageRepository extends CrudRepository<Message, Long> {

	@Query("SELECT m FROM Message m " +
			"where m.sender = :sender and m.recipient = :recipient " +
				"or m.sender = :recipient and m.recipient = :sender " +
			"order by m.posted desc")
	Collection<Message> findByRecipientOrSenderOrderByPostedDesc(
			@Param("sender") Person sender,
			@Param("recipient") Person recipient);

}
