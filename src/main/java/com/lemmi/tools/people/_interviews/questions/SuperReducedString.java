package com.lemmi.tools.people._interviews.questions;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SuperReducedString {

    public static void main(String[] args) {
//        assertEquals("a", superReducedString("adaccbbad"));
//        assertEquals("b", superReducedString("aab"));
        assertEquals("Empty String", superReducedString("abba"));
    }


    //so easy omygod -- new learning: StringBuffer.delete
    public static String superReducedString(String s) {
        StringBuffer sb = new StringBuffer(s);
        int i = 0;
        while(i < sb.length()-1) {
            char current = sb.charAt(i);
            char next = sb.charAt(i+1);

            if(current == next) {
                sb.delete(i, i+2);
                if(i > 0)
                    i = i - 1;
            } else {
                i = i+1;
            }
        }
        if(sb.toString().isEmpty())
            return "Empty String";

        return sb.toString();
    }

    public static String LEMsuperReducedString(String s) {

        var split = s.split("");

        List<Integer> iHolder = new ArrayList<>(s.length());
        int i = 0, j = 1;
        while (j < s.length()) {
            String left = split[i];
            String right = split[j];

            if (!left.equals(right)) {
                updateIHolder(i, iHolder);
                i = j;
                j = j + 1;
            } else {
                split[i] = "";
                split[j] = "";

                if (iHolder.isEmpty()) {//initial 0
                    i = i + 2;
                    j = j + 2;

                } else {
                    var lastI = lastI(iHolder);
                    if (lastI == i)
                        removeLastI(iHolder);
                    i = iHolder.isEmpty() ? j + 1 : lastI(iHolder);  //move backwards
                    j = iHolder.isEmpty() ? j + 2 : j + 1;
                }
            }
        }

        var result = String.join("", split);
        return result.equals("") ? "Empty String" : result;

    }

    private static void removeLastI(List<Integer> iHolder) {
        iHolder.remove(iHolder.size() - 1);
    }

    private static void updateIHolder(int i, List<Integer> iHolder) {

        if (iHolder.isEmpty() || lastI(iHolder) != i) {
            iHolder.add(i);
        }
    }

    private static Integer lastI(List<Integer> list) {
        return list.get(list.size() - 1);
    }

}
