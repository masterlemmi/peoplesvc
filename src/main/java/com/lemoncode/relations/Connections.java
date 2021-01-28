package com.lemoncode.relations;

import com.lemoncode.person.Person;
import lombok.*;

import javax.persistence.*;

@Entity(name = "PEOPLE_CONNECTIONS")
@Table(name = "PEOPLE_CONNECTIONS", uniqueConstraints = {@UniqueConstraint(columnNames = {"source_id", "target_id"})})
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Connections {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;
    @OneToOne
    Person source;
    @OneToOne
    Person target;
    String shortestPath;
}
