package com.lemmi.tools.people._interviews.algorithms.slindingwindow;

import org.junit.jupiter.api.Assertions;

//https://www.youtube.com/watch?v=MK-NZ4hN7rs
/*
SmallestSubArrayWthGivenSum

fiind the smallest size of a subarray whose sum >= k
1. iterate through all items
2. add each item every iteration
3. while sum matches condition
    a. then try to keep shrinking it by moving the left side of the box
    b. moving left side(left index) also means subtract the first Value (left most value in box)

 */
public class SmallestSubArrayWthGivenSum {




    public static int smallestSubArrayWithGivenSum(int[] arr, int k) {
        var smallestSubArray = new int[]{};

        return 0;

    }

    public static void main(String[] args) {
        var arr = new int[]{
                4, 2, 2, 7, 8, 1, 2, 8, 1, 0
        };

        Assertions.assertEquals(16, smallestSubArrayWithGivenSum(arr, 8));
    }

}
