package com.lemoncode.relations;

import com.lemoncode.person.GenderEnum;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum Label {
    COUSIN("cousin", "cousin", "cousin", ""),
    COUSIN_2ND("second cousin", "second cousin", "second cousin", "/parents are cousins"),
    COUSIN_3RD("third cousin", "third cousin", "third cousin", "/grandparents are cousins"),
    COUSIN_4TH("fourth cousin", "fourth cousin", "fourth cousin", "/grandparents are 2nd cousins"),
    COUSIN_5TH("fifth cousin", "fifth cousin", "fifth cousin", "/great-grandparents are 2nd cousins"),
    TITO_TITA_1("tito", "tita", "tito/tita", ""),
    TITO_TITA_2("tito", "tita", "tito/tita", "/parent's cousin"),
    TITO_TITA_3("tito", "tita", "tito/tita", "/grandparent's cousin"),
    TITO_TITA_4("tito", "tita", "tito/tita", "/grandparent's second cousin"),
    TITO_TITA_5("tito", "tita", "tito/tita", "/great great grandparent's second cousin"),
    LOLO_LOLA("lolo", "lola", "lolo/lola", ""),
    GRAND_CHILD("grandson", "grand daughter", "grandchild", "/apo"),
    PAMANGKIN("nephew", "niece", "nephew/niece", "/pamangkin"),
    PAMANGKIN_2("nephew", "niece", "nephew/niece", "/cousin's child"),
    PAMANGKIN_3("nephew", "niece", "nephew/niece", "/second cousin's child"),
    PAMANGKIN_4("nephew", "niece", "nephew/niece", "/third cousin's child"),
    LOLO_LOLA_1("lolo", "lola", "lolo/lola", " sa tuhod/great grandparent"),
    GRAND_CHILD_1("great grandson", "great grand daughter", "greate grandchild", "/apo sa tuhod"),
    LOLO_LOLA_2("lolo", "lola", "lolo/lola", " sa talampakan/great great grandparent"),
    GRAND_CHILD_2("greate great grandson", "greate great grand daughter", "greate greate grandchild", "/apo sa talampakan"),
    PARENT("father", "mother", "parent", ""),
    CHILD("son", "daughter", "child", ""),
    DIRECT_ANCESTOR("", "", "", ""),
    DIRECT_DESCENDANT("", "", "", ""),
    UNDETERMINED("", "", "", "");


    private final String male;
    private final String female;
    private final String generic;
    private final String otherDetails;

    Label(String m, String f, String g, String o) {
        this.male = m;
        this.female = f;
        this.generic = g;
        this.otherDetails = o;
    }


    static final String PARENT_OF = "is parent of";
    static final String CHILD_OF = "is child of";
    static final String SIBLING_OF = "is sibling of";
    static final String WIFE_OF = "is wife of";
    static final String HUSBAND_OF = "is husband of";

    //TODO oopposite of the follwing (e.g. NEPHEW/GRANDDAUGHETER GRANDSON);
    private static final List<String> COUSINS = Arrays.asList(CHILD_OF, SIBLING_OF, PARENT_OF);
    private static final List<String> NEPHEW_NIECE_1 = Arrays.asList( SIBLING_OF, PARENT_OF);
    private static final List<String> NEPHEW_NIECE_2 = Arrays.asList( CHILD_OF, SIBLING_OF, PARENT_OF, PARENT_OF);
     private static final List<String> SECOND_COUSINS = Arrays.asList(CHILD_OF, CHILD_OF, SIBLING_OF, PARENT_OF, PARENT_OF);
    private static final List<String> THIRD_COUSINS = Arrays.asList(CHILD_OF, CHILD_OF, CHILD_OF, SIBLING_OF, PARENT_OF, PARENT_OF, PARENT_OF);
    private static final List<String> FOURTH_COUSINS = Arrays.asList(CHILD_OF, CHILD_OF, CHILD_OF, CHILD_OF, SIBLING_OF, PARENT_OF, PARENT_OF, PARENT_OF, PARENT_OF);
    private static final List<String> FIFTH_COUSINS = Arrays.asList(CHILD_OF, CHILD_OF, CHILD_OF, CHILD_OF, CHILD_OF, SIBLING_OF, PARENT_OF, PARENT_OF, PARENT_OF, PARENT_OF, PARENT_OF);
    private static final List<String> TITO_TITA_1ST_LEVEL = Arrays.asList(CHILD_OF, SIBLING_OF);
    private static final List<String> TITO_TITA_2ND_LEVEL = Arrays.asList(CHILD_OF, CHILD_OF, SIBLING_OF, PARENT_OF);
    private static final List<String> TITO_TITA_3RD_LEVEL = Arrays.asList(CHILD_OF, CHILD_OF, CHILD_OF, SIBLING_OF, PARENT_OF, PARENT_OF);
    private static final List<String> TITO_TITA_4TH_LEVEL = Arrays.asList(CHILD_OF, CHILD_OF, CHILD_OF, CHILD_OF, SIBLING_OF, PARENT_OF, PARENT_OF, PARENT_OF);
    private static final List<String> TITO_TITA_5TH_LEVEL = Arrays.asList(CHILD_OF, CHILD_OF, CHILD_OF, CHILD_OF, CHILD_OF, SIBLING_OF, PARENT_OF, PARENT_OF, PARENT_OF, PARENT_OF);


    public static Label from(List<ConnectionsDTO.Edge> links) {
        List<String> list = links.stream().map(ConnectionsDTO.Edge::getLabel).collect(toList());

        if (list.stream().allMatch(PARENT_OF::equals)) {
            int count = list.size();
            if (count == 1) return Label.CHILD;
            else if (count == 2) return Label.GRAND_CHILD;
            else if (count == 3) return Label.GRAND_CHILD_1;
            else if (count == 4) return Label.GRAND_CHILD_2;
            else return Label.DIRECT_DESCENDANT;
        } else if (list.stream().allMatch(CHILD_OF::equals)) {
            int count = list.size();
            if (count == 1) return Label.PARENT;
            else if (count == 2) return Label.LOLO_LOLA;
            else if (count == 3) return Label.LOLO_LOLA_1;
            else if (count == 4) return Label.LOLO_LOLA_2;
            else return Label.DIRECT_ANCESTOR;
        } else if (list.equals(COUSINS)) {
            return Label.COUSIN;
        } else if (list.equals(SECOND_COUSINS)) {
            return Label.COUSIN_2ND;
        } else if (list.equals(THIRD_COUSINS)) {
            return Label.COUSIN_3RD;
        } else if (list.equals(FOURTH_COUSINS)) {
            return Label.COUSIN_4TH;
        } else if (list.equals(FIFTH_COUSINS)) {
            return Label.COUSIN_5TH;
        } else if (list.equals(TITO_TITA_1ST_LEVEL)) {
            return Label.TITO_TITA_1;
        } else if (list.equals(TITO_TITA_2ND_LEVEL)) {
            return Label.TITO_TITA_2;
        } else if (list.equals(TITO_TITA_3RD_LEVEL)) {
            return Label.TITO_TITA_3;
        } else if (list.equals(TITO_TITA_4TH_LEVEL)) {
            return Label.TITO_TITA_4;
        } else if (list.equals(TITO_TITA_5TH_LEVEL)) {
            return Label.TITO_TITA_5;
        } else if (list.equals(NEPHEW_NIECE_1)){
            return Label.PAMANGKIN;
        } else if (list.equals(NEPHEW_NIECE_2)){
            return Label.PAMANGKIN_2;
        } else {
            return UNDETERMINED;
        }
    }

    //links are all child of- whose count is greater than 4
    public static String ancestorLabel(List<ConnectionsDTO.Edge> links) {
        int count = links.size();
        if (count <= 4) throw new IllegalStateException("wrong usage. call Label.from instead");
        int greatCount = count - 2;//how many greats to use
        return "great ".repeat(greatCount) + "grandparent";
    }

    //links are all - parent of- whose count is greater than 4
    public static String descendantLabel(List<ConnectionsDTO.Edge> links, GenderEnum targetGender) {
        int count = links.size();
        if (count <= 4) throw new IllegalStateException("wrong usage. call Label.from instead");
        int greatCount = count - 2;//how many greats to use
        return "great ".repeat(greatCount) + " " + GRAND_CHILD.byGender(targetGender);
    }

    public String byGender(GenderEnum g) {
        switch (g) {
            case MALE:
                return this.male + this.otherDetails;
            case FEMALE:
                return this.female + this.otherDetails;
            default:
                return this.generic + this.otherDetails;
        }
    }

}
