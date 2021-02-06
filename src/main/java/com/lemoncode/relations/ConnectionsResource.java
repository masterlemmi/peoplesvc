package com.lemoncode.relations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/relations")
public class ConnectionsResource {


    @Autowired
    ConnectionsService connectionsService;

    @GetMapping("/family-tree/{source}")
    public ConnectionsDTO generateFamilyTree(@PathVariable("source") Long source){
        return connectionsService.findConnection(source);
    }


    @GetMapping("/between/{source}/{target}")
    public ConnectionsDTO getRelationnshipBetweenTwo(@PathVariable("source") Long source, @PathVariable("target") Long target){
        return connectionsService.findConnection(source, target);
    }

}
