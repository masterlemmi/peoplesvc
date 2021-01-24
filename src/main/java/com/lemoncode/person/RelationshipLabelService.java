package com.lemoncode.person;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Component
@ConfigurationProperties
@Getter
@Setter
public class RelationshipLabelService {

    private List<String> relationshipLabels = new ArrayList<>();

    private Map<String, Label> labelsMap = new HashMap<>();

    @PostConstruct
    public void init() {
        for (String label : this.relationshipLabels) {
            String[] array = label.split(",");
            if (array.length == 1) {
                labelsMap.put(label.toUpperCase(), new Label(label.toUpperCase()));
            } else if (array.length == 3) {
                labelsMap.put(array[0].toUpperCase(), new Label(array[1].toUpperCase(), array[2].toUpperCase()));
            } else {
                System.out.println("Cant create mapping for label " + label + " with size : " + array.length);
            }
        }

        System.out.println("MAPPING REL LABELS");
        System.out.println(labelsMap);

    }

    public static void main(String[] args) {
        String test = "boyfriend";
        String[] array = test.split(",");
        System.out.println(array.length);
    }

    public boolean isSupportedLabel(String newKey) {
        return labelsMap.containsKey(newKey);
    }

    public String getOppositeLabel(String newKey, GenderEnum gender) {
        return labelsMap.get(newKey).get(gender);
    }


    @Getter
    @AllArgsConstructor
    @ToString
    private static class Label {
        private String maleLabel;
        private String femLabel;

        Label(String label) {
            this.femLabel = label;
            this.maleLabel = label;
        }

        public String get(GenderEnum gender) {
            switch (gender){
                case MALE: return maleLabel;
                case FEMALE: return femLabel;
                default: throw new UnsupportedOperationException();
            }
        }
    }

}
