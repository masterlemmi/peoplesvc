package com.lemoncode.person;


import lombok.*;

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


}
