package com.lemoncode.person;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    private String photoUrl;
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
