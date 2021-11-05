package com.lemoncode.person;

import com.lemoncode.relationship.RelationshipDTO;
import com.lemoncode.util.DateConverter;
import org.apache.commons.text.WordUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersonMapperTest {




    @Test
    public void toPersonDTO() {
        Person person = new Person();
        person.setFirstName("Juan");
        person.setLastName("Dela cruz");
        person.setNickname("Juan");
        person.setEmail("juandelazrcuz@mgail.com");
        person.setGender(GenderEnum.MALE);
        person.setAddress("Metro Manila");
        person.setDateOfBirth(DateConverter.toLocalDate("1919-01-21"));
        person.setDateOfDeath(DateConverter.toLocalDate("1949-01-21"));
        person.setPhoto("abcdefghijk.dat");
        Person maria = new Person();
        maria.setFirstName("Maria");
        maria.setLastName("Dela Cruz");
        maria.setNickname("Marde");
        maria.setGender(GenderEnum.FEMALE);
        maria.setPhoto("http://dummyimage.comasdaf");


        person.setChildren(Set.of(maria));

        //when
        PersonDTO personDto = PersonMapper.INSTANCE.toPersonDTO(person);

        //then
        assertNotNull(personDto);
        assertEquals("Juan", personDto.getFirstName());
        assertEquals("Dela Cruz", personDto.getLastName());
        assertEquals("Juan", personDto.getNickname());
        assertEquals("juandelazrcuz@mgail.com", personDto.getEmail());
        assertEquals("M", personDto.getGender());
        assertEquals("Metro Manila", personDto.getAddress());
        assertEquals(DateConverter.toLocalDate("1919-01-21"), personDto.getDateOfBirth());
        assertEquals(DateConverter.toLocalDate("1949-01-21"), personDto.getDateOfDeath());
        assertTrue(personDto.isDeceased());
        assertNotNull(personDto.getAge());
        assertEquals("abcdefghijk.dat", personDto.getPhoto());
        assertEquals(1, personDto.getChildren().size());
        assertEquals("Juan Dela Cruz", personDto.getFullName());
        assertEquals("JDC", personDto.getInitials());


        SimplePersonDTO simple = personDto.getChildren().iterator().next();
        assertEquals("Dela Cruz", simple.getLastName());
        assertEquals("Maria", simple.getFirstName());
        assertEquals("F", simple.getGender());
        assertEquals("Maria Dela Cruz", simple.getFullName());
        assertEquals("MDC", simple.getInitials());

    }

    @Test
    public void toPersonDTOv2() {
        Person person = new Person();
        person.setId(1l);
        person.setFirstName("Juan");
        person.setLastName("Dela Cruz");
        person.setNickname("Juan");
        person.setEmail("juandelazrcuz@mgail.com");
        person.setGender(GenderEnum.MALE);
        person.setAddress("Metro Manila");
        person.setDateOfBirth(DateConverter.toLocalDate("1919-01-21"));
        person.setDateOfDeath(null);
        person.setPhoto("abcdf.png");
        Person maria = new Person();
        maria.setFirstName("Maria");
        maria.setLastName("Dela Cruz");
        maria.setNickname("Marde");
        maria.setGender(GenderEnum.FEMALE);
        maria.setPhoto("abcdef.png");


        person.setChildren(Set.of(maria));

        //when
        PersonDTO personDto = PersonMapper.INSTANCE.toPersonDTO(person);

        //then
        assertNotNull(personDto);
        assertEquals("Juan", personDto.getFirstName());
        assertEquals("Dela Cruz", personDto.getLastName());
        assertEquals("Juan", personDto.getNickname());
        assertEquals("juandelazrcuz@mgail.com", personDto.getEmail());
        assertEquals("M", personDto.getGender());
        assertEquals("Metro Manila", personDto.getAddress());

        assertNull(personDto.getDateOfDeath());
        assertFalse(personDto.isDeceased());
        assertNotNull(personDto.getAge());
        assertEquals("api/people/image/abcdf.png", personDto.getPhotoUrl());
        assertEquals(personDto.getChildren().size(), 1);
        assertEquals("Juan Dela Cruz", personDto.getFullName());
        assertEquals("JDC", personDto.getInitials());

        SimplePersonDTO simple = personDto.getChildren().iterator().next();
        assertEquals("Dela Cruz", simple.getLastName());
        assertEquals("Maria", simple.getFirstName());
        assertEquals("F", simple.getGender());
        assertEquals("Maria Dela Cruz", simple.getFullName());

        assertEquals("MDC", simple.getInitials());


    }


    @Test
    public void simplePersonDTotoPerson() {
        SimplePersonDTO dto = new SimplePersonDTO();
        dto.setId(1L);
        dto.setFirstName("Juan");
        dto.setLastName("Dela Cruz");
        dto.setNickname("Juan");
        dto.setGender("M");
        dto.setPhoto("abcdefg.jpg");

        //when
        Person person = PersonMapper.INSTANCE.toPerson(dto);

        //then
        assertNotNull(person);
        assertEquals(person.getFirstName(), "Juan");
        assertEquals(person.getLastName(), "Dela Cruz");
        assertEquals(person.getNickname(), "Juan");
        assertEquals(person.getGender(), GenderEnum.MALE);
        assertEquals("abcdefg.jpg", person.getPhoto());

    }

    @Test
    public void personDTotoPerson() {
        PersonDTO dto = new PersonDTO();
        dto.setId(1L);
        dto.setFirstName("Juan");
        dto.setLastName("Dela Cruz");
        dto.setNickname("Juan");
        dto.setGender("M");
        dto.setPhoto("abcdfh");
        dto.setEmail("test@email.com");
        dto.setAddress("Metro Manila");
        dto.setDateOfBirth(DateConverter.toLocalDate("1919-01-21"));
        dto.setDateOfDeath(DateConverter.toLocalDate("1950-01-20"));
        dto.setNotes("hello world");
        Set<SimplePersonDTO> children = new HashSet<>();
        SimplePersonDTO child1 = new SimplePersonDTO();
        child1.setFirstName("Jay");
        child1.setLastName("Lloyd");
        child1.setGender("M");
        children.add(child1);
        dto.setChildren(children);

        Set<LinkDTO> links = new HashSet<>();
        links.add(new LinkDTO(1L, "faceboook", "http:wwww"));
        dto.setLinks(links);

        List<RelationshipDTO> rels = new ArrayList<>();
        RelationshipDTO rel1 = new RelationshipDTO();
        rel1.setLabel("wife");
        Set<SimplePersonDTO> people = new HashSet<>();
        SimplePersonDTO person1 = new SimplePersonDTO();
        person1.setFirstName("Rey");
        person1.setLastName("Langit");
        person1.setGender("M");
        people.add(person1);
        rel1.setPeople(people);

        dto.setRelationships(rels);

        //when
        Person person = PersonMapper.INSTANCE.toPerson(dto);

        //then
        assertNotNull(person);
        assertEquals(person.getFirstName(), "Juan");
        assertEquals(person.getLastName(), "Dela Cruz");
        assertEquals(person.getNickname(), "Juan");
        assertEquals("test@email.com", person.getEmail());
        assertEquals(person.getGender(), GenderEnum.MALE);
        assertEquals(person.getAddress(), "Metro Manila");
        assertEquals(DateConverter.toLocalDate("1950-01-20"), person.getDateOfDeath());
        System.out.println(person.getDateOfDeath());
        assertNotNull(person.getDateOfBirth());
        assertEquals("abcdfh", person.getPhoto());
        assertEquals("hello world", person.getNotes());
        assertEquals(person.getChildren().size(), 1);
        Person child = person.getChildren().iterator().next();
        assertEquals("Jay", child.getFirstName());
        assertEquals("Lloyd", child.getLastName());
        assertEquals(GenderEnum.MALE, child.getGender());

        assertEquals(1, person.getLinks().size());
        Link link = person.getLinks().iterator().next();
        assertEquals(1L, link.getId());
        assertEquals("faceboook", link.getName());
        assertEquals("http:wwww", link.getUrl());
    }

    @Test
    public void toRelationship() {
        System.out.println("TEST");
        assertEquals("tae", "tae");
    }


}