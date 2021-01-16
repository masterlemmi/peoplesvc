package com.lemoncode.relationship;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class RelationshipLabel {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    private String label;

    //How the relationship.person_2 would refer to relationship.person_1
    // e.g. label = son oppositeLabel = father or mother (depending on gender)
    @EqualsAndHashCode.Include
    @ToString.Include
    private String oppositeLabel;

    @ManyToOne
    private Relationship relationship;

    public RelationshipLabel(String label) {
        this.label = label;
        this.oppositeLabel = label;
    }

    public RelationshipLabel(String label, String oppositeLabel) {
        this.label = label;
        this.oppositeLabel = oppositeLabel;
    }

}