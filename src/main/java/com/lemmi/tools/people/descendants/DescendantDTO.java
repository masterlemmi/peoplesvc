package com.lemmi.tools.people.descendants;

import com.lemmi.tools.people.util.CaseUtils;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DescendantDTO {

    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    private String firstName;
    @EqualsAndHashCode.Include
    private String lastName;
    private String nickname;
    private String fullName;
    private boolean expandMe; //Ui field for mat-tree use
    private String maidenName;
    private String initials;
    private String photo;
    private String photoUrl;
    private String email;
    private String address;
    private boolean deceased;
    private Boolean adopted;
    @EqualsAndHashCode.Include
    private @NonNull String gender;
    private Set<DescendantDTO> parents = new HashSet<>(); //possibility of multiple parents
    private Set<DescendantDTO> children = new HashSet<>(); //possibility of multiple parents
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
