package com.lemmi.tools.people._interviews.questions;

import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//https://www.hackerrank.com/challenges/sherlock-and-valid-string/problem?isFullScreen=true
public class SherlockValidString {

    public static String isValid(String s) {
        // Write your code here
        char[] arr = s.toCharArray();

        //how many times do the letters repeat (a:2, b:2, c:1, d:1)
        Map<Character, Integer> map = new HashMap<>();
        for (char c : arr) {
            map.merge(c, 1, Integer::sum);
        }

        //if all occurence count is the same, it is valid (a:2, b:2, c:2)
        int distinctOccurenceCount = new HashSet<>(map.values()).size();
        if (distinctOccurenceCount == 1) return "YES";

        //home many letters occur the same (2: [a, b, c] -> 3 letters, occur twice, 1: [d, e]  -> 2 letters occur once)
        Map<Integer, List<Character>> numLettersByOccurence = new HashMap<>();
        for (Map.Entry<Character, Integer> es : map.entrySet()) {
            Integer occurenceCount = es.getValue();
            if (numLettersByOccurence.get(occurenceCount) == null) {
                List<Character> newList = new ArrayList<>();
                newList.add(es.getKey());
                numLettersByOccurence.put(occurenceCount, newList);
            } else {
                numLettersByOccurence.get(occurenceCount).add(es.getKey());
            }
        }

        //there should only be two occurences ( a minority and a majority), more than that means it is invalid
        if (numLettersByOccurence.size() != 2) return "NO";

        //determine which is a minority and which is a majority
        Iterator<Map.Entry<Integer, List<Character>>> esIterator = numLettersByOccurence.entrySet().iterator();
        Integer firstOccurenceCount = esIterator.next().getKey();
        Integer secondOccurenceCount = esIterator.next().getKey();
        List<Character> firstCharList = numLettersByOccurence.get(firstOccurenceCount);
        List<Character> secondCharList = numLettersByOccurence.get(secondOccurenceCount);

        Integer majority, minority;
        if (firstCharList.size() > secondCharList.size()) {
            majority = firstOccurenceCount;
            minority = secondOccurenceCount;
        } else {
            minority = firstOccurenceCount;
            majority = secondOccurenceCount;
        }

        //scenario 3: there are multiple majorities (a:2, b:2, c:3, d:3) == INVLAID
        //ensure there is only one letter that is a minority
        if (numLettersByOccurence.get(minority).size() != 1) return "NO";


        //scenario 1: minitory letter has count 1, remove it, and everything matches (a:2, b:2, c:1)
        //find minitory (c), YES if 1,
        if (minority == 1) return "YES";

        //scenario 2: minority letter has count 1 more than majority, decrement 1, and everything matches (a:2, b:2, c:3)
        //find minitory (c), YES if minority value - majority value = 1;
        if (minority - majority == 1) return "YES";

        return "NO";


    }


    public static void main(String[] args) throws IOException {

        Assertions.assertEquals("YES", isValid("abcdefghhgfedecba"));
        Assertions.assertEquals("NO", isValid("aabbcd"));
        Assertions.assertEquals("NO", isValid("eaaabbbcccd"));
        Assertions.assertEquals("YES", isValid("aaabbbc"));
        Assertions.assertEquals("NO", isValid("cccaabbb"));


        System.out.println();
    }
}