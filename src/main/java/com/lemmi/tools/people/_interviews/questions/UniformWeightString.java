package com.lemmi.tools.people._interviews.questions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

//https://www.hackerrank.com/challenges/weighted-uniform-string/problem?isFullScreen=true
public class UniformWeightString {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\OraContent\\masterlemmi-tools\\people-svc\\src\\main\\java\\com\\lemoncode\\_interviews\\UniformWeightString.txt"));

        String s = bufferedReader.readLine();

        int queriesCount = Integer.parseInt(bufferedReader.readLine().trim());

        List<Integer> queries = IntStream.range(0, queriesCount).mapToObj(i -> {
                    try {
                        return bufferedReader.readLine().replaceAll("\\s+$", "");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(toList());


       // assertEquals(List.of("Yes"), weightedUniformStrings(s,List.of(45580)));
        assertEquals(List.of("Yes"), weightedUniformStrings("abbcccdddd", List.of(6)));
    }

    public static List<String> weightedUniformStrings(String s, List<Integer> queries) {
        // Write your code here

        Set<Long> result = new HashSet<>();
        char curCharr = s.charAt(0);
        int count = 1;
        for (int i = 1; i < s.length(); i++) {
            long weight = (long) (curCharr - 96);
            if (s.charAt(i) != curCharr) {

                result.add(weight);
                result.add(weight * count);
                System.out.println(curCharr + ": weight=" + weight + " count=" + (weight * count ));
                curCharr = s.charAt(i);
                count = 1;
            } else {
                count++;
                result.add(weight * count);
            }
        }
        long weight = (long) (curCharr - 96);
        result.add(weight);
        result.add(weight * count);

        List<String> answer = new ArrayList<>();
        for (Integer q : queries) {
            if (result.contains(q))
                answer.add("Yes");
            else
                answer.add("No");

        }

        System.out.println(result);
        return answer;
    }

}
