package com.lemoncode.descendants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/descendants")
public class DescendantsResource {


    @Autowired
    DescendantsService descendantsService;

    @PostMapping("/ancestry")
    public List<DescendantDTO> createAncestry(@RequestBody AncestryCreateRequest request) {
        return descendantsService.createAncestry(request.getAncestorId(), request.getLabel());
    }

    @GetMapping("/ancestry/{label}")
    public DescendandListResponse findAncestryBYLabel(@PathVariable String label, @RequestParam(required = false) Long descendantId) {
        try {
            return DescendandListResponse.ok(descendantsService.findAncestry(label, descendantId));
        } catch (DescendantNotFoundException e) {
            return DescendandListResponse.bad("Person wasn't found in " + label + " Ancestry");
        }
    }

    @GetMapping("/ancestry-list")
    public List<String> findAncestryLabels() {
        return descendantsService.findAncestryLabels();
    }


}
