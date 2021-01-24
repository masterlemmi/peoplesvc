package com.lemoncode.person;


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
public abstract class PersonMapper {

    public static PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);
    private static final String IMAGE_URL = "api/people/image/";
//    private static final String IMAGE_URL = "http://localhost:8081/api/people/image/";

    @Mapping(target = "parents", ignore = true) //set on personservice
    @Mapping(target = "siblings", ignore = true) //set on personservice
    @Mapping(target = "initials", ignore = true)
    @Mapping(target = "photoUrl", ignore = true)
    @Mapping(target = "fullName", expression = "java(person.getFirstName() + \" \" + person.getLastName() )")
    @Mapping(target = "age", expression = "java(computeAge(person.getDateOfBirth()))")
    @Mapping(target = "deceased", expression = "java(person.getDateOfDeath() !=  null)")
    @Mapping(target = "notes", expression = "java(person.getNotes() == null?  \"\" : person.getNotes().replaceAll(\"\\n\", \"<br/>\"))")
    public abstract PersonDTO toPersonDTO(Person person);

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
        if (p.getPhoto() != null) {
            p.setPhotoUrl(IMAGE_URL + p.getPhoto());
        }

    }

    @Mapping(target = "fullName", expression = "java(person.getFirstName() + \" \" + person.getLastName() )")
    @Mapping(target = "initials", ignore = true)
    @Mapping(target = "photoUrl", ignore = true)
    public abstract SimplePersonDTO toSimplePersonDTO(Person person);


    public SimplePersonDTO toSimplePersonDTO(PersonDTO dto) {
        if (dto == null) {
            return null;
        }

        SimplePersonDTO simplePersonDTO = new SimplePersonDTO();

        simplePersonDTO.setFirstName(dto.getFirstName());
        simplePersonDTO.setLastName(dto.getLastName());
        simplePersonDTO.setId(dto.getId());
        simplePersonDTO.setNickname(dto.getNickname());
        simplePersonDTO.setGender(dto.getGender());
        simplePersonDTO.setPhoto(dto.getPhoto());
        simplePersonDTO.setFullName( dto.getFirstName() + " " + dto.getLastName() );
        after(simplePersonDTO);
        return simplePersonDTO;
    }



    @AfterMapping
    void after(@MappingTarget SimplePersonDTO simple) {
        String[] arr = simple.getFullName().split(" ");
        String initials = Arrays.stream(arr)
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .map(s -> Character.toString(s.charAt(0)))
                .collect(Collectors.joining());
        simple.setInitials(initials);
        simple.setGender(simple.getGender().equals("MALE") ? "M" : "F");
        if (simple.getPhoto() != null) {
            simple.setPhotoUrl(IMAGE_URL + simple.getPhoto());
        }
    }

    List<RelationshipDTO> toRelationshipDTO(Map<String, Relations> relationsMap) {
        List<RelationshipDTO> list = new ArrayList<>();

        for (Map.Entry<String, Relations> entrySet : relationsMap.entrySet()) {

            RelationshipDTO dto = new RelationshipDTO();
            String label = WordUtils.capitalizeFully(entrySet.getKey());
            dto.setLabel(label);
            Set<SimplePersonDTO> people = entrySet.getValue().getPeople().stream().map(this::toSimplePersonDTO).collect(toSet());
            dto.setPeople(people);
            list.add(dto);
        }
        return list;
    }


    @Mapping(target = "fullName", expression = "java(person.getFirstName() + \" \" + person.getLastName() )")
    public abstract CacheNameDTO namesOnly(Person person);

    Integer computeAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) return null;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }





    /* ------------------ TO ENTITIES  -------------------------*/


    @Mapping(target = "gender", expression = "java(GenderEnum.from(p.getGender()))")
    public abstract Person toPerson(PersonDTO p);


    Person toPerson(SimplePersonDTO p) {
        Person person = new Person();
        person.setLastName(p.getLastName());
        person.setFirstName(p.getFirstName());
        person.setGender(GenderEnum.from(p.getGender()));
        person.setNickname(p.getNickname());
        person.setPhoto(p.getPhoto());
        person.setId(p.getId());
        return person;
    }


    @AfterMapping
    void after(@MappingTarget Person p) {
        for (Link link : p.getLinks()) {
            link.setPerson(p);
        }
    }


    @Mapping(target = "person", ignore = true)
    public abstract Link toLink(LinkDTO dto);


    Map<String, Relations> toRelationship(List<RelationshipDTO> list) {
        Map<String, Relations> map = new HashMap<>();

        for (RelationshipDTO dto : list) {
            Set<Person> personSet = dto.getPeople().stream().map(this::toPerson).collect(toSet());
            map.put(dto.getLabel(), new Relations(personSet));
        }
        return map;
    }


}