package com.lemoncode.person;


import com.lemoncode.file.FilesStorageService;
import com.lemoncode.relationship.Relations;
import com.lemoncode.relationship.RelationshipService;
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
    RelationshipService relService;

    @Autowired
    PersonMapper mapper;

    @Autowired
    FilesStorageService storageService;

    List<SimplePersonDTO> findAll() {
        return repository.findAll().stream().map(this.mapper::simplify).collect(Collectors.toList());
    }

    List<SimplePersonDTO> findAll(Set<Long> excludeIds) {
        return repository.findAll()
                .stream().filter(p -> !excludeIds.contains(p.getId())).map(mapper::simplify).collect(toList());

    }

    @Transactional
    PersonDTO findOne(int id) {
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
        return mapper.simplify(person);
    }

    private Set<SimplePersonDTO> getChildren(Set<Person> parents) {
        return repository.findChildren(parents).stream()
                .map(mapper::simplify).collect(toSet());
    }

    private Set<SimplePersonDTO> getSiblings(int id) {
        //TODO: create direct query without having to query parents first;
        Set<Person> parents = new HashSet<>(repository.findParents(id));

        return repository.findChildren(parents).stream()
                .filter(sib -> sib.getId() != id) //remove self from children
                .map(mapper::simplify).collect(Collectors.toSet());
    }

    List<SimplePersonDTO> getSample() {
        return repository.findSome().stream()
                .map(mapper::simplify).collect(toList());
    }


    Set<SimplePersonDTO> getParents(int childId) {
        List<Person> parents = repository.findParents(childId);
        return parents.stream().map(mapper::simplify).collect(toSet());
    }


    List<SimplePersonDTO> search(String query) {
        return search(query, new HashSet<>());
    }

    List<SimplePersonDTO> search(String query, Set<Long> excludeIds) {
        return repository.search(query).stream().filter(p -> !excludeIds.contains(p.getId())).map(mapper::simplify).collect(toList());
    }

    List<SimplePersonDTO> getRecent() {
        //TODO: save recents and return
        return getSample();
    }

    SimplePersonDTO createSimplePerson(SimplePersonDTO p) {
        System.out.println("received, " + p);
        Person person = new Person();
        person.setFirstName(p.getFirstName());
        person.setLastName(p.getLastName());
        person.setGender(p.getGender().startsWith("M") ? GenderEnum.MALE : GenderEnum.FEMALE);
        Person saved = this.repository.save(person);
        p.setId(saved.getId());
        p.setFullName(p.getFirstName() + " " + p.getLastName());
        return p;
    }

    public PersonDTO createPerson(PersonDTO p) {
        Person person = this.mapper.toPerson(p);
        Map<String, Relations> rel = person.getRelationships();
        for(Map.Entry<String, Relations> entrySet: rel.entrySet()){
            Relations relations = entrySet.getValue();

            Set<Person> newPeopleSet =new HashSet<>();
            for (Person other: relations.getPeople()){
                newPeopleSet.add(repository.findById(other.getId()));
            }
            relations.setPeople(newPeopleSet);
        }

        Person saved = repository.save(person);
        p.setId(saved.getId());
        return p;
    }


     void savePhoto(long id, MultipartFile file) {
        String ext = "dat";
        String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), ext);
        Person p = repository.findById(id);
        p.setPhoto(name);
        repository.save(p);
        storageService.save(id, name, file);
    }

     InputStream getPhoto(long id) throws IOException {
        Person p = repository.findById(id);

        if (p == null || p.getPhoto() == null) {
            ClassLoader classLoader = this.getClass().getClassLoader();
            return  classLoader.getResourceAsStream("no_photo.png");
        } else {
            String fileName = p.getPhoto();
            return storageService.load(id, fileName).getInputStream();
        }
    }
}
