package com.lemoncode.person;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonValidator {

   private final static Pattern ROW_PATTERN = Pattern.compile("\\w+.*/\\w+.*/[M|F]");

    static boolean isValidBatchRow(String row){
        Matcher matcher = ROW_PATTERN.matcher(row);
        return matcher.matches();

    }
}
