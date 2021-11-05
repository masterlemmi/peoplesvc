package com.lemoncode.descendants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/descendants")
public class DescendantsResource {


    @Autowired
    DescendantsService descendantsService;

    @GetMapping("/ancestor/{ancestorId}")
    public List<DescendantDTO> generateFamilyTree(@PathVariable("ancestorId") Long origin){
        return descendantsService.findDescendants(origin);
    }

}
