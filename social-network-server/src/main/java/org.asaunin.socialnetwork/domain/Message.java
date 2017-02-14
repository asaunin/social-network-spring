package org.asaunin.socialnetwork.domain;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "messages")
@NoArgsConstructor
@EqualsAndHashCode(of = {"sender", "recipient", "posted"})
@ToString(of = {"id", "body"})
public class Message implements Serializable{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sender_id")
	@Getter @Setter
	private Person sender;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "recipient_id")
	@Getter @Setter
	private Person recipient;

	@NotEmpty
	@Size(min = 1, max = 1000)
	@Getter @Setter
	private String body;

	@Column(updatable = false, nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@Getter
	private Date posted = new Date();

}
