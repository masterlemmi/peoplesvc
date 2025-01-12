package com.lemmi.tools.people.familytree;

import com.lemmi.tools.people.descendants.DescendantDTO;
import com.lemmi.tools.people.descendants.DescendantsMapper;
import com.lemmi.tools.people.person.PeopleRepository;
import com.lemmi.tools.people.person.PeopleService;
import com.lemmi.tools.people.person.Person;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/*
List only descendants following the blood relations
 */
public class FamilyTreeMakerSimple {

    PeopleService peopleService;
    PeopleRepository peopleRepository;
    //Map<Long, ConnectionsDTO.Node> nodeMap = new HashMap<>();
    List<Long> doneList = new ArrayList<>();
    @Getter
    List<DescendantDTO> descendants = new ArrayList<>();
    @Getter
    String treeLabel = "";

    public FamilyTreeMakerSimple(PeopleService peopleService,
                                 PeopleRepository peopleRepository) {
        this.peopleService = peopleService;
        this.peopleRepository = peopleRepository;
    }

    public void start(Long id) {
        if (doneList.contains(id)) return;
        doneList.add(id);
        Person person = peopleRepository.findByDescendantsByAncestorId(id);
        DescendantDTO dto = DescendantsMapper.INSTANCE.toDescendantDTO(person);
        this.descendants.add(dto);
    }

}