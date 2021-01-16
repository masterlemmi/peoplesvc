package com.lemoncode.relationship;

import com.lemoncode.person.Person;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


//This is one way as the other may have a different list
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"person1_id", "person2_id"}
        )
})
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
/*
defines reltaionship between 2 people and whta labels they have for their relationship
e.g. wife/husband/friend/etc
 */
public class Relationship {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne
    @EqualsAndHashCode.Include
    private Person person1;

    @ManyToOne
    @EqualsAndHashCode.Include
    private Person person2;


    @OneToMany(mappedBy = "relationship",
            cascade = CascadeType.ALL,
            orphanRemoval = true
//            ,            fetch = FetchType.EAGER
    )
    private Set<RelationshipLabel> relation = new HashSet<>();


    public void addRelationshipLabel(@NonNull RelationshipLabel label) {
        this.relation.add(label);
        label.setRelationship(this);
    }

    public void removeLabel(@NonNull RelationshipLabel label) {
        this.relation.remove(label);
        label.setRelationship(null);
    }

    public Relationship(Person person1, Person person2) {
        this.person1 = person1;
        this.person2 = person2;
    }
}