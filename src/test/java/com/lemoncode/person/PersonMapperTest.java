package com.lemoncode.person;

import com.lemoncode.relationship.RelationshipDTO;
import com.lemoncode.util.DateConverter;
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
        person.setLastName("DelaCruz");
        person.setNickname("Juan");
        person.setEmail("juandelazrcuz@mgail.com");
        person.setGender(GenderEnum.MALE);
        person.setAddress("Metro Manila");
        person.setDateOfBirth(DateConverter.toLocalDate("1919-01-21"));
        person.setDateOfDeath(DateConverter.toLocalDate("1949-01-21"));
        person.setPhotoUrl("http://dummyimage.comasdaf");
        Person maria = new Person();
        maria.setFirstName("Maria");
        maria.setLastName("Dela Cruz");
        maria.setNickname("Marde");
        maria.setGender(GenderEnum.FEMALE);
        maria.setPhotoUrl("http://dummyimage.comasdaf");


        person.setChildren(Set.of(maria));

        //when
        PersonDTO personDto = PersonMapper.INSTANCE.personToPersonDTO(person);

        //then
        assertNotNull(personDto);
        assertEquals("Juan", personDto.getFirstName());
        assertEquals("DelaCruz", personDto.getLastName());
        assertEquals("Juan", personDto.getNickname());
        assertEquals("juandelazrcuz@mgail.com", personDto.getEmail());
        assertEquals("M", personDto.getGender());
        assertEquals("Metro Manila", personDto.getAddress());
        assertEquals(DateConverter.toLocalDate("1919-01-21"), personDto.getDateOfBirth());
        assertEquals(DateConverter.toLocalDate("1949-01-21"), personDto.getDateOfDeath());
        assertTrue(personDto.isDeceased());
        assertNotNull(personDto.getAge());
        assertEquals("http://dummyimage.comasdaf", personDto.getPhotoUrl());
        assertEquals(1, personDto.getChildren().size());
        assertEquals("Juan DelaCruz", personDto.getFullName());
        assertEquals("JD", personDto.getInitials());


        SimplePersonDTO simple = personDto.getChildren().iterator().next();
        assertEquals("Dela Cruz", simple.getLastName());
        assertEquals("Maria", simple.getFirstName());
        assertEquals("F", simple.getGender());
        assertEquals("Maria Dela Cruz", simple.getFullName());
        assertEquals("http://dummyimage.comasdaf", simple.getPhotoUrl());
        assertEquals("MDC", simple.getInitials());

    }

    @Test
    public void test2() {
        Person person = new Person();
        person.setFirstName("Juan");
        person.setLastName("DelaCruz");
        person.setNickname("Juan");
        person.setEmail("juandelazrcuz@mgail.com");
        person.setGender(GenderEnum.MALE);
        person.setAddress("Metro Manila");
        person.setDateOfBirth(DateConverter.toLocalDate("1919-01-21"));
        person.setDateOfDeath(null);
        person.setPhotoUrl("http://dummyimage.comasdaf");
        Person maria = new Person();
        maria.setFirstName("Maria");
        maria.setLastName("Dela Cruz");
        maria.setNickname("Marde");
        maria.setGender(GenderEnum.FEMALE);
        maria.setPhotoUrl("http://dummyimage.comasdaf");


        person.setChildren(Set.of(maria));

        //when
        PersonDTO personDto = PersonMapper.INSTANCE.personToPersonDTO(person);

        //then
        assertNotNull(personDto);
        assertEquals(personDto.getFirstName(), "Juan");
        assertEquals(personDto.getLastName(), "DelaCruz");
        assertEquals(personDto.getNickname(), "Juan");
        assertEquals(personDto.getEmail(), "juandelazrcuz@mgail.com");
        assertEquals(personDto.getGender(), "M");
        assertEquals(personDto.getAddress(), "Metro Manila");

        assertNull(personDto.getDateOfDeath());
        assertFalse(personDto.isDeceased());
        assertNotNull(personDto.getAge());
        assertEquals(personDto.getPhotoUrl(), "http://dummyimage.comasdaf");
        assertEquals(personDto.getChildren().size(), 1);
        assertEquals(personDto.getFullName(), "Juan DelaCruz");
        assertEquals(personDto.getInitials(), "JD");

        SimplePersonDTO simple = personDto.getChildren().iterator().next();
        assertEquals(simple.getLastName(), "Dela Cruz");
        assertEquals(simple.getFirstName(), "Maria");
        assertEquals(simple.getGender(), "F");
        assertEquals(simple.getFullName(), "Maria Dela Cruz");
        assertEquals(simple.getPhotoUrl(), "http://dummyimage.comasdaf");
        assertEquals(simple.getInitials(), "MDC");

    }


    @Test
    public void simplePersonDTotoPerson() {
        SimplePersonDTO dto = new SimplePersonDTO();
        dto.setId(1L);
        dto.setFirstName("Juan");
        dto.setLastName("DelaCruz");
        dto.setNickname("Juan");
        dto.setGender("M");
        dto.setPhotoUrl("http://dummyimage.comasdaf");

        //when
        Person person = PersonMapper.INSTANCE.toPerson(dto);

        //then
        assertNotNull(person);
        assertEquals(person.getFirstName(), "Juan");
        assertEquals(person.getLastName(), "DelaCruz");
        assertEquals(person.getNickname(), "Juan");
        assertEquals(person.getGender(), GenderEnum.MALE);
        assertEquals(person.getPhotoUrl(), "http://dummyimage.comasdaf");

    }

    @Test
    public void personDTotoPerson() {
        PersonDTO dto = new PersonDTO();
        dto.setId(1L);
        dto.setFirstName("Juan");
        dto.setLastName("DelaCruz");
        dto.setNickname("Juan");
        dto.setGender("M");
        dto.setPhotoUrl("http://dummyimage.comasdaf");
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
        assertEquals(person.getLastName(), "DelaCruz");
        assertEquals(person.getNickname(), "Juan");
        assertEquals("test@email.com", person.getEmail());
        assertEquals(person.getGender(), GenderEnum.MALE);
        assertEquals(person.getAddress(), "Metro Manila");
        assertEquals(DateConverter.toLocalDate("1950-01-20"), person.getDateOfDeath());
        System.out.println(person.getDateOfDeath());
        assertNotNull(person.getDateOfBirth());
        assertEquals(person.getPhotoUrl(), "http://dummyimage.comasdaf");
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