package com.lemoncode.descendants;

import com.lemoncode.person.Person;
import lombok.*;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "ANCESTRY")
@Table(name = "ANCESTRY")
@Getter
@Setter
@NoArgsConstructor
//@ToString(onlyExplicitlyIncluded = true)
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ancestry {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @EqualsAndHashCode.Include
//    @ToString.Include
//    private Long id;
    @Id
    private Long id;
    @OneToOne
    @MapsId
    private Person ancestor;
    @Column(unique = true)
    private String label;
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "DESCENDANTS",
            joinColumns = @JoinColumn(name = "ancestor_id"),
            inverseJoinColumns = @JoinColumn(name = "descendant_id")
    )
    private Set<Person> descendants = new HashSet<>();

    public void addDescendant(Person descendant) {
        descendants.add(descendant);
        descendant.getAncestries().add(this);
    }

    public void removeDescendant(Person descendant) {
        descendants.remove(descendant);
        descendant.getAncestries().remove(this);
    }

    public void clearDescendants() {
        descendants.clear();
    }

    public void addAllDescendants(Set<Person> descendants) {
        if (CollectionUtils.isEmpty(descendants)) return;
        descendants.forEach(this::addDescendant);
    }
}
