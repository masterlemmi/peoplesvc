package com.lemoncode.descendants;

import com.lemoncode.dfs.Graph;
import com.lemoncode.person.PeopleRepository;
import com.lemoncode.person.Person;
import com.lemoncode.person.PersonNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
    AncestryRepository ancestryRepository;

    Map<String, List<DescendantDTO>> fakeCache = new ConcurrentHashMap<>();


    @Transactional
    public List<DescendantDTO> createAncestry(Long originAncestor, String ancestryNameFromReq) {
        String ancestryName = ancestryNameFromReq.replaceAll("\\s+", "_");
        Ancestry ancestryDB = ancestryRepository.findByLabel(ancestryName);
        if (ancestryDB != null && !ancestryDB.getId().equals(originAncestor)) {
            //there is an existing ancestry with the given name. delete existign ancestry
            ancestryRepository.deleteById(ancestryDB.getId());
        }
        Person theAncestor = peopleRepository.findByDescendantsByAncestorId(originAncestor);
        if (theAncestor == null) throw new PersonNotFoundException();
        DescendantDTO dto = DescendantsMapper.INSTANCE.toDescendantDTO(theAncestor);

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

        fakeCache.put(theAncestor.getId() + "", List.of(dto));
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

    public List<DescendantDTO> findAncestry(String label, Long descendantId) {
        Ancestry ancestryFrom = ancestryRepository.findByLabel(label);

        if (ancestryFrom == null) {
            throw new AncestryNotFoundException();
        }

        if (descendantId != null){
            //check that he is a descendant
            ancestryFrom.getDescendants().stream().map(Person::getId).filter(id -> id.equals(descendantId)).findAny()
                    .orElseThrow(DescendantNotFoundException::new);
        }

        String ancestorIdDescendantId = "" + ancestryFrom.getId() + (descendantId == null ? "" : String.valueOf(descendantId));
        List<DescendantDTO> dto = fakeCache.get(ancestorIdDescendantId);
        if (dto == null) {
            Long ancestorId = ancestryFrom.getId();
            List<DescendantDTO> expandedDto = setExpandValue(createAncestry(ancestorId, label), descendantId);
            fakeCache.put(ancestorIdDescendantId, expandedDto);
            return expandedDto;
        }
        return dto;
    }

    private List<DescendantDTO> setExpandValue(List<DescendantDTO> ancestry, Long descendantId) {
        if (descendantId == null) return ancestry;
        return List.of(Graph.findPaths(ancestry.get(0), descendantId));
    }

    public void clearFakeCache() {
        this.fakeCache.clear();
    }

    @Transactional
    public void deleteAll() {
        int deleted = ancestryRepository.deleteAll();
        System.out.println("Deleted " + deleted + " rows");
    }

    public List<String> findAncestryLabels() {
        return ancestryRepository.findAll().stream().map(Ancestry::getLabel).collect(Collectors.toList());
    }

    @Async
    public void recreateDescendants() {
        List<Ancestry> ancestryList = ancestryRepository.findAll();

        ancestryList.forEach(x ->
                createAncestry(x.getId(), x.getLabel()
                ));
    }

}