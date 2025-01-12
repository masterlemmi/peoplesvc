package com.lemmi.tools.people.Testss;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Result {
    public static void main(String[] args) {
        System.out.println(findReviewScore("fastdeliveryokayproduct", Arrays.asList("eryoka", "yo", "eli")));
    }

    public static int findReviewScore(String review, List<String> prohibitedWords) {
        Set<String> prohibitedUpperCase = prohibitedWords.stream().map(String::toUpperCase).collect(Collectors.toSet());
        String rev = review.toUpperCase();
        if (!hasBadwords(rev, prohibitedUpperCase)) {
            return review.length();
        }

        int maxLength = 0;
        int lastGoodIndex = -1;
        for (int i = 0; i < review.length(); i++) {

            boolean alreadyHasBadwords = false;
            for (int j = i +1; j < review.length()+1; j++) {
                if (alreadyHasBadwords) {
                    i = lastGoodIndex;
                    break;
                }
                String substring = rev.substring(i, j);
                boolean hasBadwords = hasBadwords(substring, prohibitedUpperCase);
                alreadyHasBadwords = hasBadwords;
                if (!hasBadwords){
                    System.out.println(substring.length() + "---" + substring + " " + lastGoodIndex);
                    lastGoodIndex = j-1;
                }
                if (!hasBadwords && substring.length() > maxLength){
                    maxLength = substring.length();
                }
            }
        }

        return maxLength;
    }

    private static boolean hasBadwords(String review, Set<String> prohibitedUpperCase) {
        return prohibitedUpperCase.stream().anyMatch(review::contains);
    }


}

/*

    public static int findReviewScore(String review, List<String> prohibitedWords) {
        Set<String> prohibitedUpperCase = prohibitedWords.stream().map(String::toUpperCase).collect(Collectors.toSet());
        String rev = review.toUpperCase();
        if (!hasBadwords(rev, prohibitedUpperCase)) {
            return review.length();
        }

        int maxLength = 0;
        for (int i = 0; i < review.length(); i++) {

            boolean alreadyHasBadwords = false;
            for (int j = i +1; j < review.length()+1; j++) {
                if (alreadyHasBadwords)
                    break;
                String substring = rev.substring(i, j);
                boolean hasBadwords = hasBadwords(substring, prohibitedUpperCase);
                alreadyHasBadwords = hasBadwords;
//                if (allGood){
//                    System.out.println(substring.length() + "---" + substring);
//                }
                if (!hasBadwords && substring.length() > maxLength){
                    maxLength = substring.length();
                }
            }
        }

        return maxLength;
    }

    private static boolean hasBadwords(String review, Set<String> prohibitedUpperCase) {
        return prohibitedUpperCase.stream().anyMatch(review::contains);
    }

 */