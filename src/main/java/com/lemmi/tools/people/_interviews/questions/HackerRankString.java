package com.lemmi.tools.people._interviews.questions;

public class HackerRankString {

    public static void main(String[] args) {
        System.out.println(hackerrankInString("hackerworld"));
    }
    public static String hackerrankInString(String s) {
        // Write your code here
        char[] hWord = "hackerrank".toCharArray();
        char[] sArr = s.toCharArray();

        int index = 0;


        for (int i = 0; i < sArr.length; i++){
            char curr = sArr[i];
            char letter = hWord[index];



            if (letter == curr){
//                System.out.println(letter + " v " + curr + " at " + i);
                if (index == 9)
                    return "YES";
                index++;
            }

        }

        return "NO";
    }
}
