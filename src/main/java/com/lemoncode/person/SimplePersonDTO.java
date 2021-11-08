package com.lemoncode.person;


import com.lemoncode.util.CaseUtils;
import lombok.*;
import org.apache.commons.text.WordUtils;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//simpler versioni of PersonDTO to avoid infnitnite recursion
//used for parents/children/relationships
public class SimplePersonDTO {


    @EqualsAndHashCode.Include
    private Long id;
    @EqualsAndHashCode.Include
    private String firstName;
    @EqualsAndHashCode.Include
    private String lastName;
    @EqualsAndHashCode.Include
    private String nickname;
    @EqualsAndHashCode.Include
    private @NonNull String gender;
    private String fullName;
    private String photo;
    private String photoUrl;
    private String initials;


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
