package com.lemoncode.person;

import com.lemoncode.relationship.RelationshipDTO;
import com.lemoncode.util.CaseUtils;
import lombok.*;
import org.apache.commons.text.WordUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PersonDTO {

    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    private String firstName;
    @EqualsAndHashCode.Include
    private String lastName;
    private String nickname;
    private String fullName;
    private String maidenName;
    private String initials;
    private String photo;
    private String photoUrl;
    private String email;
    private String address;
    private Integer age;
    private boolean deceased;
    private Boolean adopted;
    @EqualsAndHashCode.Include
    private @NonNull String gender;
    @EqualsAndHashCode.Include
    private LocalDate dateOfBirth;
    private LocalDate dateOfDeath;
    private Set<SimplePersonDTO> parents = new HashSet<>(); //possibility of multiple parents
    private Set<SimplePersonDTO> children = new HashSet<>(); //possibility of multiple parents
    private Set<SimplePersonDTO> siblings = new HashSet<>(); //possibility of multiple parents
    private List<RelationshipDTO> relationships = new ArrayList<>(); //possibility of multiple parents
    private Set<LinkDTO> links = new HashSet<>();
    private String notes;
    private String displayText;

    public void setFirstName(String firstName) {
        if (firstName != null) {
            this.firstName = CaseUtils.capitalizeName(firstName);
        }
    }

    public void setLastName(String lastName) {
        if (lastName != null) {
            this.lastName = CaseUtils.capitalizeName(lastName);
        }
    }

    public void setFullName(String fullName) {
        if (fullName != null) {
            this.fullName = CaseUtils.capitalizeName(fullName);
        }
    }

}
