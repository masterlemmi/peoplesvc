package com.lemoncode.person;


import com.lemoncode.descendants.DescendantsService;
import com.lemoncode.file.FilesStorageService;
import com.lemoncode.relations.ConnectionsService;
import com.lemoncode.relationship.Relations;
import com.lemoncode.relationship.RelationshipDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
public class PeopleService {
    private final static Logger LOGGER = Logger.getLogger(PeopleService.class.getName());
    @Autowired
    PeopleRepository repository;

    @Autowired
    PersonMapper mapper;

    @Autowired
    FilesStorageService storageService;

    @Autowired
    RelationshipLabelService labelService;

    @Autowired
    ConnectionsService connectionsService;

    @Autowired
    DescendantsService descendantsService;

    List<SimplePersonDTO> findAllSimple() {
        return repository.findAll().stream().map(this.mapper::toSimplePersonDTO).collect(Collectors.toList());
    }

    List<SimplePersonDTO> findAllSimple(Set<Long> excludeIds) {
        return repository.findAll()
                .stream().filter(p -> !excludeIds.contains(p.getId())).map(mapper::toSimplePersonDTO).collect(toList());
    }

    @Transactional
    public List<PersonDTO> findAll() {
        List<Person> people = repository.findAllJoined();
        List<PersonDTO> dtos = new ArrayList<>();
        for (Person person : people) {
            dtos.add(findOne(person.getId()));
        }
        return dtos;
    }

    @Transactional
   public PersonDTO findOne(Long id) {
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
                .filter(sib -> !sib.getId().equals(id)) //remove self from children
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

    public List<SimplePersonDTO> search(String query, Set<Long> excludeIds) {
        return repository.search(query).stream().filter(p -> !excludeIds.contains(p.getId())).map(mapper::toSimplePersonDTO).collect(toList());
    }

    List<SimplePersonDTO> getRecent() {
        //TODO: findConnection recents and return
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

        connectionsService.deleteAll(); //delete prebuilt connections as there may have been changes

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
        connectionsService.deleteAll(); //delete prebuilt connections as there may have been changes
        connectionsService.clearFakeCache();
        descendantsService.clearFakeCache();
        descendantsService.deleteAll();
        p.setId(saved.getId());
        return p;
    }

    public List<SimplePersonDTO> save(List<Person> people) {

        List<SimplePersonDTO> createdList =  repository.save(people).stream().map(this.mapper::toSimplePersonDTO).collect(Collectors.toList());

        connectionsService.deleteAll(); //delete prebuilt connections as there may have been changes

        return createdList;

    }

    public void addMainInOppositeRelationship(SimplePersonDTO main, List<RelationshipDTO> relationships) {
        for (RelationshipDTO reldto : relationships) {
            String label = reldto.getLabel().toUpperCase();

            if (!labelService.isSupportedLabel(label))
                continue;

            String oppositeLabel = labelService.getOppositeLabel(label, GenderEnum.from(main.getGender()));
            Set<SimplePersonDTO> people = reldto.getPeople();

            for (SimplePersonDTO simpleDTO : people) {
                PersonDTO other = this.findOne(simpleDTO.getId());

                Map<String, Set<SimplePersonDTO>> otherRels = other.getRelationships()
                        .stream().collect(toMap(rel -> rel.getLabel().toUpperCase(), RelationshipDTO::getPeople));

                Set<SimplePersonDTO> existingRel = otherRels.get(oppositeLabel);

                if (CollectionUtils.isEmpty(existingRel)) {
                    Set<SimplePersonDTO> set = new HashSet<>();
                    set.add(main);
                    otherRels.put(oppositeLabel, set);
                } else { //there is an existing list of people so just add main to the list
                    existingRel.add(main);
                }

                //convert the map back to list
                List<RelationshipDTO> newList = otherRels.entrySet().stream()
                        .map(e -> new RelationshipDTO(e.getKey(), e.getValue()))
                        .collect(toList());

                other.setRelationships(newList);

                this.createPerson(other);

            }

        }
    }

    public void addMainAsChild(SimplePersonDTO main, Set<SimplePersonDTO> parents) {

        for (SimplePersonDTO parent : parents) {
            PersonDTO other = this.findOne(parent.getId());
            Set<SimplePersonDTO> otherChildren = other.getChildren();

            boolean alreadyAdded = otherChildren.stream().anyMatch(x -> x.getId() == main.getId());

            if (alreadyAdded)
                continue;
            otherChildren.add(main);
            this.createPerson(other);
        }

    }


}