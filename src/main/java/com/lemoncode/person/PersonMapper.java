package com.lemoncode.person;


import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.stream.Collectors;


@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public abstract class PersonMapper {

    public static PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mapping(target = "relationships", ignore = true) //set on personservice
    @Mapping(target = "parents", ignore = true) //set on personservice
    @Mapping(target = "siblings", ignore = true) //set on personservice
    @Mapping(target = "initials", ignore = true)
    @Mapping(target = "fullName", expression = "java(person.getFirstName() + \" \" + person.getLastName() )")
    @Mapping(target = "age", expression = "java(computeAge(person.getDateOfBirth()))")
    @Mapping(target = "deceased", expression = "java(person.getDateOfDeath() !=  null)")
    @Mapping(target = "notes", expression = "java(replaceNewLines(person.getNotes()))")
    public abstract PersonDTO personToPersonDTO(Person person);

    @Mapping(target = "fullName", expression = "java(person.getFirstName() + \" \" + person.getLastName() )")
    @Mapping(target = "initials", ignore = true)
//    @Mapping(target = "relationshipLabel", ignore=true)
    public abstract SimplePersonDTO simplify(Person person);

    @AfterMapping
    void after(@MappingTarget SimplePersonDTO simp) {
        String[] arr = simp.getFullName().split(" ");
        String initials = Arrays.stream(arr)
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .map(s -> Character.toString(s.charAt(0)))
                .collect(Collectors.joining());
        simp.setInitials(initials);
        simp.setGender(simp.getGender().equals("MALE") ? "M" : "F");
    }

    @AfterMapping
    void after(@MappingTarget PersonDTO p) {
        String[] arr = p.getFullName().split(" ");
        String initials = Arrays.stream(arr)
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .map(s -> Character.toString(s.charAt(0)))
                .collect(Collectors.joining());
        p.setInitials(initials);
        p.setGender(p.getGender().equals("MALE") ? "M" : "F");
    }

    Integer computeAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return null;
        return Period.between(LocalDate.now(), dateOfBirth).getYears();
    }

    String replaceNewLines(String notes) {
        if (notes != null) {
            return notes.replaceAll("\n", "<br/>");
        }
        return "";
    }

    @Mapping(target = "fullName", expression = "java(person.getFirstName() + \" \" + person.getLastName() )")
    public abstract CacheNameDTO namesOnly(Person person);


}