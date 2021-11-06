package com.lemoncode.descendants;

import com.lemoncode.person.SimplePersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/descendants")
public class DescendantsResource {


    @Autowired
    DescendantsService descendantsService;

    @PostMapping("/ancestry")
    public List<DescendantDTO> createAncestry(@RequestBody AncestryCreateRequest request){
        return descendantsService.createAncestry(request.getAncestorId(), request.getLabel());
    }

    @GetMapping("/ancestry/{label}")
    public List<DescendantDTO> findAncestryBYLabel(@PathVariable String label){
        return descendantsService.findAncestry(label);
    }

    @GetMapping("/ancestry-list")
    public List<String> findAncestryLabels(){
        return descendantsService.findAncestryLabels();
    }



}
