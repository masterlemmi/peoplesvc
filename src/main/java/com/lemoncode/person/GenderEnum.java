package com.lemoncode.person;



import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.function.Predicate;

public enum GenderEnum {
    MALE("M"), FEMALE("F"), UNDETERMINED("-");

    String abbr;

    GenderEnum(String abbr){
        this.abbr = abbr;
    }

    public static GenderEnum from(String gender) {

        return Arrays.stream(values()).filter(determine(gender)).findAny().orElse(UNDETERMINED);
    }

    private static Predicate<GenderEnum> determine(String gender) {
        return genderEnum -> !StringUtils.isEmpty(gender) && gender.toUpperCase().startsWith(genderEnum.abbr);
    }



}