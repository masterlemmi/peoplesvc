package com.lemoncode._interviews.algorithms.sort;

public class MergeSort {

    public static void main(String[] args) {
        int[] a = { 5, 1, 6, 2, 3, 4 };
        mergeSort(a, a.length);
        for (int i = 0; i < a.length; i++)
            System.out.println(a[i]);
    }

    public static void mergeSort(int[] origArr, int n) {
        if (n < 2)
            return;
        int mid = n / 2;
        int[] l = new int[mid];
        int[] r = new int[n - mid];

        for (int i = 0; i < mid; i++) {
            l[i] = origArr[i];
        }
        for (int i = mid; i < n; i++) {
            r[i - mid] = origArr[i];
        }
        mergeSort(l, mid);
        mergeSort(r, n - mid);

        merge(origArr, l, r, mid, n - mid);
    }

    public static void merge(int[] origArr, int[] leftArr, int[] rightArr, int leftIndex, int rightIndex) {

        int i = 0, j = 0, origArrIndex = 0;

        while (i < leftIndex && j < rightIndex) {

            if (leftArr[i] <= rightArr[j])
                origArr[origArrIndex++] = leftArr[i++];
            else
                origArr[origArrIndex++] = rightArr[j++];

        }

        //for when the rightArr is longer
        while (i < leftIndex)
            origArr[origArrIndex++] = leftArr[i++];

        //for when the rightArr is longer
        while (j < rightIndex)
            origArr[origArrIndex++] = rightArr[j++];
    }
}