package com.lemoncode.util;

import org.apache.commons.text.WordUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CaseUtils {

    public static String capitalizeName(String name){
        if (name == null) return null;

        List<String> ordinalLastNames = Arrays.asList("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X");

        String capitalized = WordUtils.capitalizeFully(name.toLowerCase(Locale.ROOT).trim());
        String allCaps = name.toUpperCase(Locale.ROOT).trim();
        for (String order: ordinalLastNames){
            String regex = ".* " + order + "$";
            if (allCaps.matches(regex)) {
                int spaceIndex = capitalized.lastIndexOf(" " );
                String cutName = capitalized.substring(0, spaceIndex + 1);
                return cutName + order;
            }
        }

        return capitalized;

    }

    public static void main(String[] args) {
        String name = "john RobERT Ix";
        System.out.println(capitalizeName(name));
    }
}
