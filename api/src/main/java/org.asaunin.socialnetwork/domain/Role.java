package org.asaunin.socialnetwork.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@EqualsAndHashCode
public class Role implements Serializable {

    public static final Role ADMIN = new Role(1L, "ROLE_ADMIN", "admin");
    public static final Role USER = new Role(2L, "ROLE_USER", "user");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Column(length = 50, unique = true)
    @Getter @Setter
    private String name;

    @Column(length = 50)
    @Getter @Setter
    private String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Person> users;

    public Role(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

}
