package com.lemoncode.relationship;

import com.lemoncode.person.Person;
import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


//This is one way as the other may have a different list
@Entity
//@Table(uniqueConstraints = {
//        @UniqueConstraint(
//                columnNames = {"person_id", "label"}
//        )
//})
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

/*
Since a nested Map collection is not allowed (e.g. Map<"bestfriend", Set<People>>) this serves as workaround to collect the sets of people belonging to a relationship label"
 */
public class Relations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    public Relations(Set<Person> people){
        if (people!=null){
            this.people.addAll(people);
        }
    }

    @ManyToMany(
            cascade = CascadeType.ALL
    )
    @JoinTable(name = "relations_people",
            joinColumns = @JoinColumn(name = "relations_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private Set<Person> people = new HashSet<>();

    public void addPerson(@NonNull Person person) {
        this.people.add(person);
    }

    public void addPerson(@NonNull Person... person) {
        Arrays.stream(person).forEach(this::addPerson);
    }

    public void removePerson(@NonNull Person person) {
        this.people.remove(person);
    }



}