package com.lemoncode.person;


import com.lemoncode.file.FilesStorageService;
import com.lemoncode.relationship.Relations;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
class PeopleService {
    private final static Logger LOGGER = Logger.getLogger(PeopleService.class.getName());
    @Autowired
    PeopleRepository repository;

    @Autowired
    PersonMapper mapper;

    @Autowired
    FilesStorageService storageService;

    @Autowired
    RelationshipLabelService labelService;

    List<SimplePersonDTO> findAll() {
        return repository.findAll().stream().map(this.mapper::toSimplePersonDTO).collect(Collectors.toList());
    }

    List<SimplePersonDTO> findAll(Set<Long> excludeIds) {
        return repository.findAll()
                .stream().filter(p -> !excludeIds.contains(p.getId())).map(mapper::toSimplePersonDTO).collect(toList());

    }

    @Transactional
    PersonDTO findOne(Long id) {
        Person person = repository.findById(id);
        PersonDTO dto = mapper.toPersonDTO(person);

        if (dto != null) {
            dto.setParents(getParents(id));
            dto.setSiblings(getSiblings(id));
        }

        return dto;
    }

    SimplePersonDTO findOneSimple(int id) {
        Person person = repository.findById(id);
        return mapper.toSimplePersonDTO(person);
    }

    private Set<SimplePersonDTO> getChildren(Set<Person> parents) {
        return repository.findChildren(parents).stream()
                .map(mapper::toSimplePersonDTO).collect(toSet());
    }

    private Set<SimplePersonDTO> getSiblings(Long id) {
        //TODO: create direct query without having to query parents first;
        Set<Person> parents = new HashSet<>(repository.findParents(id));

        return repository.findChildren(parents).stream()
                .filter(sib -> sib.getId() != id) //remove self from children
                .map(mapper::toSimplePersonDTO).collect(Collectors.toSet());
    }

    List<SimplePersonDTO> getSample() {
        return repository.findSome().stream()
                .map(mapper::toSimplePersonDTO).collect(toList());
    }


    Set<SimplePersonDTO> getParents(Long childId) {
        List<Person> parents = repository.findParents(childId);
        return parents.stream().map(mapper::toSimplePersonDTO).collect(toSet());
    }


    List<SimplePersonDTO> search(String query) {
        return search(query, new HashSet<>());
    }

    List<SimplePersonDTO> search(String query, Set<Long> excludeIds) {
        return repository.search(query).stream().filter(p -> !excludeIds.contains(p.getId())).map(mapper::toSimplePersonDTO).collect(toList());
    }

    List<SimplePersonDTO> getRecent() {
        //TODO: save recents and return
        return getSample();
    }

    SimplePersonDTO createSimplePerson(SimplePersonDTO p) {

        Person person = new Person();
        person.setFirstName(p.getFirstName());
        person.setLastName(p.getLastName());
        person.setGender(p.getGender().startsWith("M") ? GenderEnum.MALE : GenderEnum.FEMALE);
        Person saved = this.repository.save(person);
        p.setId(saved.getId());
        p.setFullName(p.getFirstName() + " " + p.getLastName());
        return p;
    }


    String savePhoto(long id, MultipartFile file) {
        String name = RandomStringUtils.randomAlphanumeric(20);
        Person p = repository.findById(id);
        p.setPhoto(name);
        repository.save(p);
        storageService.save(name, file);
        return name;
    }

    InputStream getPhoto(String fileName) throws IOException {

        try {
            return storageService.load(fileName).getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            ClassLoader classLoader = this.getClass().getClassLoader();
            return classLoader.getResourceAsStream("no_photo.png");

        }
    }


    public PersonDTO createPerson(PersonDTO p) {
        Person person = this.mapper.toPerson(p);
        Map<String, Relations> rel = person.getRelationships();
        for (Map.Entry<String, Relations> entrySet : rel.entrySet()) {
            Relations relations = entrySet.getValue();
            Set<Person> newPeopleSet = new HashSet<>();
            for (Person other : relations.getPeople()) {
                newPeopleSet.add(repository.findById(other.getId()));
            }
            relations.setPeople(newPeopleSet);
        }

        Person saved = repository.save(person);
        p.setId(saved.getId());
        return p;
    }

    public List<SimplePersonDTO> save(List<Person> people) {

        return repository.save(people).stream().map(this.mapper::toSimplePersonDTO).collect(Collectors.toList());

    }
}