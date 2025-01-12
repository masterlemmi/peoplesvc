package com.lemmi.tools.people._interviews.algorithms.sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class MergeSort {

    public static void main(String[] args) {
        int[] a = { 5, 1, 6, 2, 3, 4 , 9,8,7,10,11,-1,-2, 0};
        mergeSort(a, a.length);

        System.out.println("Result");
        printArray(a);
    }

    public static void mergeSort(int[] origArr, int n) {
       // printArray(origArr);
        if (origArr.length == 1)
            return;

        int mid = n / 2;

        int[] l = Arrays.copyOfRange(origArr, 0, mid);
        int[] r = Arrays.copyOfRange(origArr, mid, origArr.length);

        mergeSort(l, mid);
        mergeSort(r, n - mid);

        merge(origArr, l, r);
    }

    public static void merge(int[] origArr, int[] leftArr, int[] rightArr) {


//        printArray(leftArr, "(" + leftIndex +")");
//        System.out.println(" + ");
//        printArray(rightArr, "(" + rightIndex +")");

        int i = 0, j = 0, origArrIndex = 0;

        while (i < leftArr.length && j < rightArr.length) {

            if (leftArr[i] <= rightArr[j])
                origArr[origArrIndex++] = leftArr[i++];
            else
                origArr[origArrIndex++] = rightArr[j++];

        }

        //for when the rightArr is longer
        while (i < leftArr.length)
            origArr[origArrIndex++] = leftArr[i++];

        //for when the rightArr is longer
        while (j < rightArr.length)
            origArr[origArrIndex++] = rightArr[j++];

     //   System.out.print("=");
     //   printArray(origArr);
        System.out.println();
    }

    static void printArray(int[] arr){
        printArray(arr, "");
    }
    static void printArray(int[] arr, String suffix){
        var list = Arrays.stream(arr)
                .boxed()
                .map(String::valueOf)
                .collect(Collectors.joining(" "));

        System.out.println(list + " " + suffix);
    }
}