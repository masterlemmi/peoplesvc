package com.lemoncode.person;


import com.lemoncode.relationship.Relations;
import com.lemoncode.relationship.RelationshipDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;


@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public abstract class PersonMapper {

    public static PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mapping(target = "parents", ignore = true) //set on personservice
    @Mapping(target = "siblings", ignore = true) //set on personservice
    @Mapping(target = "initials", ignore = true)
    @Mapping(target = "photoUrl", expression = "java(setPhotoUrl(person.getId()))")
    @Mapping(target = "fullName", expression = "java(person.getFirstName() + \" \" + person.getLastName() )")
    @Mapping(target = "age", expression = "java(computeAge(person.getDateOfBirth()))")
    @Mapping(target = "deceased", expression = "java(person.getDateOfDeath() !=  null)")
    @Mapping(target = "notes", expression = "java(replaceNewLines(person.getNotes()))")
    public abstract PersonDTO toPersonDTO(Person person);

    @Mapping(target = "fullName", expression = "java(person.getFirstName() + \" \" + person.getLastName() )")
    @Mapping(target = "initials", ignore = true)
    @Mapping(target = "photoUrl", expression = "java(setPhotoUrl(person.getId()))")
    public abstract SimplePersonDTO simplify(Person person);

    public List<RelationshipDTO> toRelationshipDTO(Map<String, Relations> relationsMap) {
        List<RelationshipDTO> list = new ArrayList<>();

        for (Map.Entry<String, Relations> entrySet : relationsMap.entrySet()) {

            RelationshipDTO dto = new RelationshipDTO();
            dto.setLabel(entrySet.getKey());
            Set<SimplePersonDTO> people = entrySet.getValue().getPeople().stream().map(this::simplify).collect(toSet());
            dto.setPeople(people);
            list.add(dto);
        }
        return list;
    }

    String setPhotoUrl(Long id) {
        if (id == null)
            return null;
        return "api/people/" + id + "/image";
    }


    public Map<String, Relations> toRelationship(List<RelationshipDTO> list) {
        Map<String, Relations> map = new HashMap<>();

        for (RelationshipDTO dto : list) {
            Set<Person> personSet = dto.getPeople().stream().map(this::toPerson).collect(toSet());
            map.put(dto.getLabel(), new Relations(personSet));
        }

        return map;


    }

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

    @AfterMapping
    void after(@MappingTarget Person p) {
        for (Link link : p.getLinks()) {
            link.setPerson(p);
        }
    }

    Integer computeAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return null;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    String replaceNewLines(String notes) {
        if (notes != null) {
            return notes.replaceAll("\n", "<br/>");
        }
        return "";
    }

    @Mapping(target = "fullName", expression = "java(person.getFirstName() + \" \" + person.getLastName() )")
    public abstract CacheNameDTO namesOnly(Person person);


    @Mapping(target = "gender", expression = "java(GenderEnum.from(p.getGender()))")
    @Mapping(target = "photo",ignore = true)
    public abstract Person toPerson(PersonDTO p);


    Person toPerson(SimplePersonDTO p) {
        Person person = new Person();
        person.setLastName(p.getLastName());
        person.setFirstName(p.getFirstName());
        person.setGender(GenderEnum.from(p.getGender()));
        person.setNickname(p.getNickname());
        person.setId(p.getId());
        return person;
    }

    @Mapping(target = "person", ignore = true)
    public abstract Link toLink(LinkDTO dto);

}