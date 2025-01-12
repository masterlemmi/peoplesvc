package com.lemmi.tools.people._interviews.algorithms.slindingwindow;

import org.junit.jupiter.api.Assertions;
//https://www.youtube.com/watch?v=MK-NZ4hN7rs
/*
Find the max  sum subarray of a fixed size k

1. add every iteration
2. if reached k-1 (end of first box),
    then set the maximum (because we already hve first box, and every iteration is new box)
    also subtract first Value from runningSUm in preparation for the next iteration addition.

 */
public class MaxSumSubArray {


    public static int findMaxSumSubArray(int[] arr, int k) {
        int foundMax = Integer.MIN_VALUE;
        int currentSum = 0;
        for (int i = 0; i < arr.length; i++) {
            currentSum += arr[i];
            if (i >= k-1) {
                foundMax = Math.max(foundMax, currentSum);
                var firstValue = arr[i - (k -1)];
                currentSum = currentSum  - firstValue;

            }
        }

        return foundMax;

    }

    public static void main(String[] args) {
        var arr = new int[]{
                4, 2, 1, 7, 8, 1, 2, 8, 1, 0
        };

        Assertions.assertEquals(16, findMaxSumSubArray(arr, 3));
    }

}
