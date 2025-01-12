package com.lemmi.tools.people.Testss;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TEStss2 {
    public static void main(String[] args) {

        //System.out.println(average(Arrays.asList(1,2,3,4)));
        System.out.println(        locateEarliestMonth(Arrays.asList(1, 3, 2, 3)));
    }

    public static int locateEarliestMonth(List<Integer> stockPrice) {
        Double allSum = sum(stockPrice);
        Double min = Double.MAX_VALUE;
        int monthAnswer = Integer.MAX_VALUE;

        double lastSum = 0;

        for (int i = 1; i < stockPrice.size(); i++){
            double prevSum = lastSum + stockPrice.get(i);
            double nextSum = allSum - prevSum;

            double prevAve = Math.floor(prevSum/i);
            double nextAve = Math.floor(nextSum/stockPrice.size() -i);

            double abs = Math.abs(prevAve-nextAve);
            if (abs < min ){
                monthAnswer = i;
                min = abs;
            }
        }

        System.out.println(monthAnswer);
        return monthAnswer;
    }

    private static double sum(List<Integer> list){
      return  list.stream().mapToInt(Integer::intValue).sum();
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
