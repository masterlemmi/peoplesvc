package com.lemoncode.descendants;

import com.lemoncode.dijkrsta.ShortestPathService;
import com.lemoncode.familytree.FamilyTreeMaker;
import com.lemoncode.familytree.FamilyTreeMakerSimple;
import com.lemoncode.person.*;
import com.lemoncode.relations.Connections;
import com.lemoncode.relations.ConnectionsDTO;
import com.lemoncode.relations.ConnectionsRepository;
import com.lemoncode.relations.Label;
import com.lemoncode.relationship.RelationshipDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DescendantsService {
    @Autowired
    PeopleRepository peopleRepository;

    @Autowired
    PeopleService peopleService;

    @Autowired
    ConnectionsRepository connectionsRepository;

    @Autowired
    ShortestPathService shortestPathService;

    @Autowired
    AncestryRepository ancestryRepository;

    Map<Long, DescendantDTO> fakeCache = new ConcurrentHashMap<>();


    public List<DescendantDTO> oldfindDescendants(Long origin) {
        FamilyTreeMakerSimple familyTreeMakerSimple = new FamilyTreeMakerSimple(peopleService, peopleRepository);
        familyTreeMakerSimple.start(origin);
        return familyTreeMakerSimple.getDescendants();
    }


    @Transactional
    public List<DescendantDTO> createAncestry(Long originAncestor, String ancestryName) {
        Ancestry ancestryDB = ancestryRepository.findByLabel(ancestryName);
        if (ancestryDB != null && !ancestryDB.getId().equals(originAncestor)){
            //there is an existing ancestry with the given name. delete existign ancestry
            ancestryRepository.deleteById(ancestryDB.getId());
        }
        Person theAncestor = peopleRepository.findByDescendantsByAncestorId(originAncestor);
        if (theAncestor == null) throw new PersonNotFoundException();
        DescendantDTO dto = DescendantsMapper.INSTANCE.toDescendantDTO(theAncestor);

//
//        Ancestry ancestryFrom = ancestryRepository.findByLabel(ancestryName);
//        if (ancestryFrom == null){
//            ancestryFrom = new Ancestry();
//            Person person = new Person();
//            person.setId(theAncestor.getId());
//            ancestryFrom.setAncestor(person);
//            ancestryFrom.setLabel(ancestryName);
//        }

        Ancestry ancestryFrom = theAncestor.getAncestry();
        if (ancestryFrom == null) {
            ancestryFrom = new Ancestry();
            ancestryFrom.setAncestor(theAncestor);
            theAncestor.setAncestry(ancestryFrom);
        }
        ancestryFrom.setLabel(ancestryName);

        Set<Person> descendants = retrieveDescendants(theAncestor);
        ancestryFrom.addAllDescendants(descendants);

        peopleRepository.save(theAncestor);

        fakeCache.put(theAncestor.getId(), dto);
        return List.of(dto);
    }

    private Set<Person> retrieveDescendants(Person theAncestor) {
        Set<Person> descendants = new HashSet<>();
        for (Person child : theAncestor.getChildren()) {
            descendants.add(child);
            descendants.addAll(retrieveDescendants(child));
        }
        return descendants;
    }

    public List<DescendantDTO> findAncestry(String label) {
        Ancestry ancestryFrom = ancestryRepository.findByLabel(label);
        if (ancestryFrom == null) {
            throw new AncestryNotFoundException();
        } else {
            Long ancestorId = ancestryFrom.getId();
            if (fakeCache.containsKey(ancestorId)) {
                return List.of(fakeCache.get(ancestorId));
            } else {
                return createAncestry(ancestorId, label);
            }
        }

    }

    public void clearFakeCache() {
        this.fakeCache.clear();
    }

    public void deleteAll() {
        int deleted = ancestryRepository.deleteAll();
        System.out.println("Deleted " + deleted + " rows");
    }

    public List<String> findAncestryLabels() {
        return ancestryRepository.findAll().stream().map(Ancestry::getLabel).collect(Collectors.toList());
    }
}