package com.lemmi.tools.people.Testss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TEStss {
    public static void main(String[] args) {

        //System.out.println(average(Arrays.asList(1,2,3,4)));
        System.out.println(        locateEarliestMonth(Arrays.asList(1, 3, 2, 3)));
    }

    public static int locateEarliestMonth(List<Integer> stockPrice) {

        Double min = Double.MAX_VALUE;
        int monthAnswer = Integer.MAX_VALUE;
        for (int i = 1; i < stockPrice.size(); i++){
           double prevAve = average(stockPrice.subList(0, i));
       //     System.out.println(prevAve);
            double newAve = average(stockPrice.subList(i, stockPrice.size()));
         //   System.out.println(newAve);
            double abs = Math.abs(prevAve-newAve);
            if (abs < min ){
                monthAnswer = i;
                min = abs;
            }
        }

        System.out.println(monthAnswer);
        return monthAnswer;
    }

    private static double average(List<Integer> list){
     //   System.out.println(list);
//        int sum = 0;
//        for (int num: list){
//            sum+=num;
//        }
//
//        double ave = sum/list.size();
//
        double ave = list.stream().mapToInt(Integer::intValue).average()
                .getAsDouble();

        return Math.floor(ave);
    }
}
