package com.lemoncode.relations;

import com.lemoncode.person.Person;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    public String getLabel(List<ConnectionsDTO.Edge> links, Person target) {
        Label label = Label.from(links);
        switch (label) {
            case DIRECT_DESCENDANT:
                return Label.ancestorLabel(links);
            case DIRECT_ANCESTOR:
                return Label.descendantLabel(links, target.getGender());
            default:
                return label.byGender(target.getGender());
        }
    }
}
