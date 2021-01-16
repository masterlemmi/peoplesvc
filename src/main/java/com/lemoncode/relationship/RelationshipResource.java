package com.lemoncode.relationship;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/relationships")
public class RelationshipResource {
    private final static Logger LOGGER = Logger.getLogger(RelationshipResource.class.getName());

    @Autowired
    private RelationshipService relService;


    @GetMapping
    public List<RelationshipDTO> getRelationshipsOfPerson(@NonNull @RequestParam("person") Integer personId) {
        return relService.getRelationships(personId);
    }


    @GetMapping("/{id}")
    public List<Relationship> getAllByPersonID(@PathParam("id") Integer personId) {

        return relService.getRelationashipsTest(personId);
    }


}
