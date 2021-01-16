package com.lemoncode.person;

import com.lemoncode.relationship.RelationshipDTO;
import com.lemoncode.relationship.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toSet;


@RestController
@RequestMapping(value = "/people")
public class PeopleResource {
    private final static Logger LOGGER = Logger.getLogger(PeopleResource.class.getName());

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private RelationshipService relService;

    @PostMapping("/simple")
    public SimplePersonDTO createSimplePerson(@RequestBody SimplePersonDTO p) {
        return peopleService.createSimplePerson(p);
    }


    @GetMapping
    public List<SimplePersonDTO> find(@RequestParam(value = "q", required = false) String query, @RequestParam(value = "exclude", required = false) String excludeIds) {

        if (query != null) {
            if (excludeIds != null && !excludeIds.trim().isEmpty()) {
                Set<Long> exclude = Arrays.stream(excludeIds.split(",")).map(Long::parseLong).collect(toSet());
                return peopleService.search(query, exclude);
            } else {
                return peopleService.search(query);
            }
        } else {
            if (excludeIds != null && !excludeIds.trim().isEmpty()) {
                Set<Long> exclude = Arrays.stream(excludeIds.split(",")).map(Long::parseLong).collect(toSet());
                return peopleService.findAll(exclude);
            } else {
                return peopleService.findAll();
            }
        }

    }

    @GetMapping("/recent")
    public List<SimplePersonDTO> getRecent() {
        return peopleService.getRecent();
    }


    @GetMapping("/simple/{id}")
    public SimplePersonDTO findSimpleById(@PathVariable("id") int id) {
        return peopleService.findOneSimple(id);
    }

    @GetMapping("/{id}")
    public PersonDTO findPersonById(@PathVariable("id") int id) {
        return peopleService.findOne(id);
    }

    @GetMapping("/{id}/relations")
    public List<RelationshipDTO> findPeopleWithRelationshipsAs(@PathVariable("id") int id, @RequestParam(value = "label", required = false) String label) {
        if (label == null) {
            return relService.getRelationships(id);
        }
        return List.of(relService.getRelationships(id, label));
    }

}
