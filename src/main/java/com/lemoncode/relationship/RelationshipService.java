package com.lemoncode.relationship;


import com.lemoncode.person.PeopleRepository;
import com.lemoncode.person.PersonMapper;
import com.lemoncode.person.SimplePersonDTO;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class RelationshipService {
//    private final static Logger LOGGER = Logger.getLogger(RelationshipService.class.getName());
//    @Autowired
//    PeopleRepository repository;
//
//    @Autowired
//    RelationshipRepository relRepo;
//
//    @Autowired
//    PersonMapper mapper;
//
//    @Transactional
//    List<Relations> getRelationashipsTest(int id) {
//        List<Relations> list = relRepo.findByPersonId(id);
//        list.forEach(System.out::println);
//        return list;
//    }
//
//    @Transactional
//    public List<RelationshipDTO> getRelationships(int id) {
//        List<Relations> rels = relRepo.findByPersonId(id);
//        System.out.println("------> relationships found for id" + 3 + " = " + rels.size());
//        Map<String, Set<SimplePersonDTO>> map = new HashMap<>();
//        for (Relations rel : rels) {
//            if (id == rel.getPerson1().getId()) {
//                //id = the main person so we want the name of the other person
//
//                Set<RelationshipLabel> labels = rel.getRelation(); //
//                System.out.println("--------+> relation labels found:" + labels.size());
//                labels.forEach(x -> System.out.println("----> " + x.getLabel()));
//                for (RelationshipLabel label : labels) {
//                    SimplePersonDTO other = PersonMapper.INSTANCE.simplify(rel.getPerson2());
//                    //  other.setRelationshipLabel(label.getLabel());
//
//                    if (map.get(label.getLabel()) == null) {
//                        Set<SimplePersonDTO> others = new HashSet<>();
//                        others.add(other);
//                        map.put(label.getLabel(), others);
//                    } else {
//                        map.get(label.getLabel()).add(other);
//                    }
//                }
//            } else {
//
//                Set<RelationshipLabel> labels = rel.getRelation(); //
//                System.out.println("--------> relation labels found:" + labels.size());
//                for (RelationshipLabel label : labels) {
//                    SimplePersonDTO other = PersonMapper.INSTANCE.simplify(rel.getPerson1());
//                    System.out.println("---> label: " + label.getLabel() + " " + label.getOppositeLabel());
//                    //other.setRelationshipLabel(label.getOppositeLabel());
//                    if (map.get(label.getOppositeLabel()) == null) {
//                        Set<SimplePersonDTO> others = new HashSet<>();
//                        others.add(other);
//                        map.put(label.getOppositeLabel(), others);
//                    } else {
//                        map.get(label.getOppositeLabel()).add(other);
//                    }
//                }
//            }
//        }
//
//        return map.entrySet().stream()
//                .map(entry -> {
//                    String label = StringUtils.capitalize(entry.getKey());
//                    return new RelationshipDTO(label, entry.getValue());
//                }).collect(Collectors.toList());
//
//    }
//
//    @Transactional
//    public RelationshipDTO getRelationships(int personId, @NonNull String labelOfInterest) {
//        List<Relations> rels = relRepo.findByPersonId(personId);
//        Map<String, Set<SimplePersonDTO>> map = new HashMap<>();
//        for (Relations rel : rels) {
//            if (personId == rel.getPerson1().getId()) {
//                //id = the main person (person1) so we want the name of the other person (person2)
//                SimplePersonDTO other = PersonMapper.INSTANCE.simplify(rel.getPerson2());
//                Set<RelationshipLabel> labels = rel.getRelation(); //
//                for (RelationshipLabel label : labels) {
//
//                    if (!labelOfInterest.equals(label.getLabel())) //we only want the people you consider as label(e.g. friend)
//                        continue;
//
//                    if (map.get(label.getLabel()) == null) {
//                        Set<SimplePersonDTO> others = new HashSet<>();
//                        others.add(other);
//                        map.put(label.getLabel(), others);
//                    } else {
//                        map.get(label.getLabel()).add(other);
//                    }
//                }
//            } else {
//                //personID = the other person(person2) so we want the name of person1
//                SimplePersonDTO other = PersonMapper.INSTANCE.simplify(rel.getPerson1());
//                Set<RelationshipLabel> labels = rel.getRelation(); //
//                for (RelationshipLabel label : labels) {
//
//                    if (!labelOfInterest.equals(label.getOppositeLabel())) //we only want the people that consider you as label(e.g. friend)
//                        continue;
//
//                    if (map.get(label.getOppositeLabel()) == null) {
//                        Set<SimplePersonDTO> others = new HashSet<>();
//                        others.add(other);
//                        map.put(label.getOppositeLabel(), others);
//                    } else {
//                        map.get(label.getOppositeLabel()).add(other);
//                    }
//                }
//            }
//        }
//
//        LOGGER.info("Retrieved " + map.entrySet().size() + " relations labeled as " + labelOfInterest);
//        return map.entrySet().stream()
//                .map(entry -> {
//                    String label = StringUtils.capitalize(entry.getKey());
//                   return new RelationshipDTO(label, entry.getValue());
//                })
//                .findFirst().orElse(null);
//
//
//    }
//
//    public void addRelationships(List<RelationshipDTO> relationships) {
//        Map<SimplePersonDTO, Set<String>> labelsByPerson = new HashMap<>();
//
//        for (RelationshipDTO r: relationships){
//            String label = r.getLabel();
//            for (SimplePersonDTO p: r.getPeople()){
//                Set<String> labels = labelsByPerson.get(p);
//
//                if (labels == null){
//                    labels = new HashSet<>();
//                    labels.add(label);
//                }
//            }
//        }
//
//    }
}
