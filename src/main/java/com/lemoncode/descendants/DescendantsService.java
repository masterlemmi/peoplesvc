package com.lemoncode.descendants;

import com.lemoncode.dfs.Graph;
import com.lemoncode.person.CacheService;
import com.lemoncode.person.PeopleRepository;
import com.lemoncode.person.Person;
import com.lemoncode.person.PersonNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DescendantsService {
    private final PeopleRepository peopleRepository;
    private final AncestryRepository ancestryRepository;
    private final CacheService cacheService;


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

        Ancestry ancestryFromAncestor = theAncestor.getAncestry();
        if (ancestryFromAncestor == null) {
            ancestryFromAncestor = new Ancestry();
            ancestryFromAncestor.setAncestor(theAncestor);
            theAncestor.setAncestry(ancestryFromAncestor);
        }
        ancestryFromAncestor.setLabel(ancestryName);

        Set<Person> descendants = retrieveDescendants(theAncestor);
        ancestryFromAncestor.addAllDescendants(descendants);

        peopleRepository.save(theAncestor);

        cacheService.addDescendantsCache(theAncestor.getId() + "", List.of(dto));
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

        if (descendantId != null) {
            //check that he is a descendant
            ancestryFrom.getDescendants().stream().map(Person::getId).filter(id -> id.equals(descendantId)).findAny()
                    .orElseThrow(DescendantNotFoundException::new);
        }

        String ancestorIdDescendantId = "" + ancestryFrom.getId() + (descendantId == null ? "" : String.valueOf(descendantId));
        List<DescendantDTO> dto = cacheService.getDescendantsCache(ancestorIdDescendantId);
        if (dto == null) {
            Long ancestorId = ancestryFrom.getId();
            List<DescendantDTO> expandedDto = setExpandValue(createAncestry(ancestorId, label), descendantId);
            cacheService.addDescendantsCache(ancestorIdDescendantId, expandedDto);
            return expandedDto;
        }
        return dto;
    }

    private List<DescendantDTO> setExpandValue(List<DescendantDTO> ancestry, Long descendantId) {
        if (descendantId == null) return ancestry;
        return List.of(Graph.findPaths(ancestry.get(0), descendantId));
    }

    public List<String> findAncestryLabels() {
        return ancestryRepository.findAll().stream().map(Ancestry::getLabel).collect(Collectors.toList());
    }

}