package com.lemoncode.familytree;

import com.lemoncode.descendants.DescendantDTO;
import com.lemoncode.descendants.DescendantsMapper;
import com.lemoncode.person.*;
import com.lemoncode.relations.ConnectionsDTO;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public void start(Long id){
        if (doneList.contains(id)) return;
        doneList.add(id);
        Person person = peopleRepository.findByDescendantsByAncestorId(id);
        DescendantDTO dto = DescendantsMapper.INSTANCE.toDescendantDTO(person);
        this.descendants.add(dto);
    }

}