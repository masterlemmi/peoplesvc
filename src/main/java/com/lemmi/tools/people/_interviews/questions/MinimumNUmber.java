package com.lemmi.tools.people._interviews.questions;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MinimumNUmber {


    public static void main(String[] args) {
        System.out.println(

                minimumNumber(3, "AUzs-nV"));
    }

public static int minimumNumber(int n, String password) {
    // Return the minimum number of characters to make the password strong
    var hasDigit = password.matches(".*\\d+.*");
    var hasLowerCase = password.matches(".*[a-z].*");
    var hasUpperCase = password.matches(".*[A-Z].*");
    var hasSymbol = password.matches(".*[!@#$%^&*()\\-+].*");

    int required = 0;
    var boolArr = new boolean[]{hasDigit, hasLowerCase, hasUpperCase, hasSymbol};
    for (boolean bool : boolArr) {
        if (!bool) required++;
    }

    if (password.length() + required < 6) {
        return required + (6 - required - password.length());
    }


    return required;

}

}
