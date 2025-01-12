package com.lemmi.tools.people._interviews.algorithms.sort;

import java.util.Arrays;

public class MergeSortCount {

    public static void main(String[] args) {
        int[] a = { 5, 1, 6, 2, 3, 4 };
       int swaps =  mergeSort(a, a.length);
        System.out.println("Did " + swaps + " swaps");
        for (int i = 0; i < a.length; i++)
            System.out.println(a[i]);
    }

    public static int mergeSort(int[] origArr, int arrLength) {
        if (origArr.length == 1)
            return 0;

        int mid = arrLength / 2;

        int[] l = Arrays.copyOfRange(origArr, 0, mid);
        int[] r = Arrays.copyOfRange(origArr, mid, origArr.length);

        int lCount = mergeSort(l, mid);
        int rCount = mergeSort(r, arrLength - mid);

        int mergeCount = merge(origArr, l, r, mid, arrLength - mid);

        return lCount + rCount + mergeCount;
    }

    public static int merge(int[] origArr, int[] leftArr, int[] rightArr, int leftIndex, int rightIndex) {

        int i = 0, j = 0, origArrIndex = 0, swaps = 0;

        while (i < leftIndex && j < rightIndex) {

            if (leftArr[i] <= rightArr[j])
                origArr[origArrIndex++] = leftArr[i++];
            else {
                origArr[origArrIndex++] = rightArr[j++];
                swaps += (origArr.length /2 - (leftIndex + i)); //middle of array minus the leftIndex
            }

        }

        //for when the rightArr is longer
        while (i < leftIndex)
            origArr[origArrIndex++] = leftArr[i++];

        //for when the rightArr is longer
        while (j < rightIndex)
            origArr[origArrIndex++] = rightArr[j++];

        return swaps;
    }
}