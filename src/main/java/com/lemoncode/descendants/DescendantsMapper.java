package com.lemoncode.descendants;


import com.lemoncode.person.Person;
import com.lemoncode.relationship.Relations;
import com.lemoncode.relationship.RelationshipDTO;
import org.apache.commons.text.WordUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;


@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public abstract class DescendantsMapper {

    public static DescendantsMapper INSTANCE = Mappers.getMapper(DescendantsMapper.class);
    private static final String IMAGE_URL = "api/people/image/";
//    private static final String IMAGE_URL = "http://localhost:8081/api/people/image/";

    @Mapping(target = "initials", ignore = true)
    @Mapping(target = "photoUrl", ignore = true)
    @Mapping(target = "expandMe", ignore = true)
    @Mapping(target = "parents", ignore = true)
    @Mapping(target = "fullName", expression = "java(person.getFirstName() + \" \" + person.getLastName() )")
    @Mapping(target = "deceased", expression = "java(person.getDateOfDeath() !=  null)")
    public abstract DescendantDTO toDescendantDTO(Person person);


    @AfterMapping
    void after(@MappingTarget DescendantDTO p) {
        String[] arr = p.getFullName().split(" ");
        String initials = Arrays.stream(arr)
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .map(s -> Character.toString(s.charAt(0)))
                .collect(Collectors.joining());
        p.setInitials(initials);
        p.setGender(p.getGender().equals("MALE") ? "M" : "F");
        if (p.getPhoto() != null) {
            p.setPhotoUrl(IMAGE_URL + p.getPhoto());
        }

    }

}