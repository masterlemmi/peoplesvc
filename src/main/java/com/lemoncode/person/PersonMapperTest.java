package com.lemoncode.person;

import com.lemoncode.util.DateConverter;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class PersonMapperTest {
    @Test
    public void test() {
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


}