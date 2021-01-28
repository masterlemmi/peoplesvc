package com.lemoncode.relations;

import com.lemoncode.person.GenderEnum;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum Label {
    COUSIN("cousin", "cousin", "cousin"),
    COUSIN_2ND("second cousin", "second cousin", "second cousin"),
    COUSIN_3RD("third cousin", "third cousin", "third cousin"),
    TITO_TITA("tito", "tita", "tito/tita"),
    LOLO_LOLA("lolo", "lola", "lolo/lola"),
    UNDETERMINED("", "", "");


    private String male;
    private String female;
    private String generic;

    Label(String m, String f, String g) {
        this.male = m;
        this.female = f;
        this.generic = g;
    }


    static final String PARENT_OF = "is parent of";
    static final String CHILD_OF = "is child of";
    static final String SIBLING_OF = "is sibling of";
    static final String WIFE_OF = "is wife of";
    static final String HUSBAND_OF = "is husband of";

    private static final List<String> COUSINS = Arrays.asList(CHILD_OF, SIBLING_OF, PARENT_OF);
    private static final List<String> SECOND_COUSINS = Arrays.asList(CHILD_OF, CHILD_OF, SIBLING_OF, PARENT_OF, PARENT_OF);
    private static final List<String> THIRD_COUSINS = Arrays.asList(CHILD_OF, CHILD_OF, SIBLING_OF, PARENT_OF, PARENT_OF); //TODO:
    private static final List<String> TITO_TITA_1 = Arrays.asList(CHILD_OF, SIBLING_OF);
    private static final List<String> TITO_TITA_2 = Arrays.asList(CHILD_OF, CHILD_OF, SIBLING_OF, PARENT_OF);
    private static final List<String> LOLO_LOLA_L = Arrays.asList(CHILD_OF, CHILD_OF);
    private static final List<String> LOLO_LOLA_L = Arrays.asList(CHILD_OF, CHILD_OF);

    public static Label from(List<ConnectionsDTO.Edge> links) {
        List<String> list = links.stream().map(ConnectionsDTO.Edge::getLabel).collect(toList());
        if (list.equals(COUSINS)) {
            return Label.COUSIN;

        } else if (list.equals(SECOND_COUSINS)) {
            return Label.COUSIN_2ND;
        } else if (list.equals(THIRD_COUSINS)) {
            return Label.COUSIN_3RD;
        } else if (list.equals(TITO_TITA_1) || list.equals(TITO_TITA_2)) {
            return Label.TITO_TITA;
        } else if (list.equals(LOLO_LOLA_L)) {
            return Label.LOLO_LOLA;
        } else {
            return UNDETERMINED;
        }
    }

    public String byGender(GenderEnum g) {
        switch (g) {
            case MALE:
                return this.male;
            case FEMALE:
                return this.female;
            default:
                return this.generic;
        }
    }

}
