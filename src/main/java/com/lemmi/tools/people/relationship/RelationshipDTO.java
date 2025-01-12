package com.lemmi.tools.people.relationship;


import com.lemmi.tools.people.person.SimplePersonDTO;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
//simpler versioni of RelationshipDTO to avoid infnitnite recursion
//used for parents/children/relationships
public class RelationshipDTO {
    private String label;
    private Set<SimplePersonDTO> people;
}
