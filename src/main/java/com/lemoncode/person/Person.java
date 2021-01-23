package com.lemoncode.person;

import com.lemoncode.relationship.Relations;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity(name = "PEOPLE")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    @Column(nullable = false)
    private String firstName;
    @EqualsAndHashCode.Include
    @Column(nullable = false)
    private String lastName;
    private String nickname;
    private String address;
    private String photo;
    private String email;
    private String notes;
    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    @Column(nullable = false)
    private @NonNull GenderEnum gender;
    @EqualsAndHashCode.Include
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;


    //query this table to get parents and/or children
    @ManyToMany
    @JoinTable(name = "children",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    private Set<Person> children = new HashSet<>(); //possibility of multiple parents


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "relationships",
            joinColumns = {@JoinColumn(name = "person_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "relations_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "label")
    private Map<String, Relations> relationships = new HashMap<>();

    public void addRelationship(String label, Relations rel){
        this.relationships.put(label, rel);
    }

    public void removeRelationship(String label){
        this.relationships.remove(label);
    }


    public void addChild(@NonNull Person person) {
        this.children.add(person);
    }

    public void addChild(@NonNull Person... person) {
        Arrays.stream(person).forEach(this::addChild);
    }

    public void removeChild(@NonNull Person person) {
        this.children.remove(person);
    }

    @OneToMany(mappedBy = "person",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Link> links = new HashSet<>();

    public void addLink(@NonNull Link link) {
        this.links.add(link);
        link.setPerson(this);
    }

    public void removeLink(@NonNull Link link) {
        this.links.remove(link);
        link.setPerson(null);
    }


}
