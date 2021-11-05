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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

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


    public List<DescendantDTO> findDescendants(Long origin) {
        FamilyTreeMakerSimple familyTreeMakerSimple = new FamilyTreeMakerSimple(peopleService, peopleRepository);
        familyTreeMakerSimple.start(origin);
        return familyTreeMakerSimple.getDescendants();
    }
}