package com.lemoncode.person;


import com.lemoncode.descendants.DescendantsService;
import com.lemoncode.file.FilesStorageService;
import com.lemoncode.relations.ConnectionsRepository;
import com.lemoncode.relationship.Relations;
import com.lemoncode.relationship.RelationshipDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PeopleService {
    private final PeopleRepository repository;
    private final PersonMapper mapper;
    private final FilesStorageService storageService;
    private final RelationshipLabelService labelService;
    private final CacheService cacheService;
    private final ConnectionsRepository connectionsRepository;

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
        p.setId(saved.getId());

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

    InputStream getPhoto(String fileName) {

        try {
            return storageService.load(fileName).getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
            ClassLoader classLoader = this.getClass().getClassLoader();
            return classLoader.getResourceAsStream("no_photo.png");

        }
    }


    @Transactional
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
        afterSavePersonTasks();
        return p;
    }

    @Transactional
    public List<SimplePersonDTO> save(List<Person> people) {

        List<SimplePersonDTO> createdList = repository.save(people).stream().map(this.mapper::toSimplePersonDTO).collect(Collectors.toList());
        afterSavePersonTasks();
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

            boolean alreadyAdded = otherChildren.stream().anyMatch(x -> x.getId().equals(main.getId()));

            if (alreadyAdded)
                continue;
            otherChildren.add(main);
            this.createPerson(other);
        }

    }


    public FamilyDTO createFamily(FamilyDTO family) {

        if (CollectionUtils.isEmpty(family.getParents())) {
            throw new IllegalArgumentException("Parents must not be empty, otherwise how family?");
        }

        if (family.getParents().size() > 2) {
            throw new IllegalArgumentException("Family Parents should only be 1 or 2");
        }

        family.getParents().stream().filter(parent -> family.getChildren().contains(parent))
                .findAny()
                .ifPresent(x -> {
                    throw new IllegalArgumentException("Parent is listed in children list. ");
                });

        family.getChildren().stream().filter(child -> family.getParents().contains(child))
                .findAny()
                .ifPresent(x -> {
                    throw new IllegalArgumentException("Child is listed in parent list. ");
                });

        for (SimplePersonDTO parent : family.getParents()) {
            Person parentDB = repository.findById(parent.getId());
            if (parentDB == null) throw new PersonNotFoundException("Parent not in db");

            for (SimplePersonDTO child : family.getChildren()) {
                Person childDB = repository.findById(child.getId());
                parentDB.getChildren().add(childDB);
            }
            repository.save(parentDB);
        }

        return family;
    }

    @Transactional
    public void afterSavePersonTasks() {
        connectionsRepository.deleteAll(); //delete prebuilt connections as there may have been changes
        cacheService.clearConnections();
        cacheService.clearDescendants();
    }
}