package org.asaunin.socialnetwork.repository;

import org.asaunin.socialnetwork.domain.Message;
import org.asaunin.socialnetwork.domain.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

	@Query("SELECT m " +
			"FROM Message m " +
			"WHERE m.sender = :sender AND m.recipient = :recipient " +
			"   OR m.sender = :recipient AND m.recipient = :sender " +
			"ORDER BY m.posted DESC")
	List<Message> findByRecipientOrSenderOrderByPostedDesc(
			@Param("sender") Person sender,
			@Param("recipient") Person recipient);

	@Query("SELECT m " +
			"FROM Message m " +
			"WHERE m.id IN (" +
			"   SELECT MAX(l.id) " +
			"   FROM Message l " +
			"   WHERE l.sender = :person OR l.recipient = :person " +
			"   GROUP BY " +
			"       CASE " +
			"           WHEN l.recipient = :person THEN l.sender " +
			"           WHEN l.sender = :person THEN l.recipient " +
			"           ELSE :person " +
			"       END) " +
			"ORDER BY m.posted DESC")
	List<Message> findLastMessagesByPerson(@Param("person") Person person);

}
