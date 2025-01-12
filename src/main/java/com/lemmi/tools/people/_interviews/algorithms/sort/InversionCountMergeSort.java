package com.lemmi.tools.people._interviews.algorithms.sort;
//https://www.geeksforgeeks.org/inversion-count-in-array-using-merge-sort/
//https://www.youtube.com/watch?v=owZhw-A0yWE
import java.util.Arrays;

public class InversionCountMergeSort {

    // Function to count the number of inversions
    // during the merge process
    private static int mergeAndCount(int[] arr, int l,
                                     int m, int r) {

        // Left subarray
        int[] leftArr = Arrays.copyOfRange(arr, l, m + 1);

        // Right subarray
        int[] rightArr = Arrays.copyOfRange(arr, m + 1, r + 1);

        int i = 0, j = 0, k = l, swaps = 0;

        while (i < leftArr.length && j < rightArr.length) {
            if (leftArr[i] <= rightArr[j])
                arr[k++] = leftArr[i++];
            else {
                arr[k++] = rightArr[j++];
                swaps += (m + 1) - (l + i);
            }
        }
        while (i < leftArr.length)
            arr[k++] = leftArr[i++];
        while (j < rightArr.length)
            arr[k++] = rightArr[j++];
        return swaps;
    }

    // Merge sort function
    private static int mergeSortAndCount(int[] arr, int l,
                                         int r) {

        // Keeps track of the inversion count at a
        // particular node of the recursion tree
        int count = 0;

        if (l < r) {
            int m = (l + r) / 2;

            // Total inversion count = left subarray count
            // + right subarray count + merge count

            // Left subarray count
            count += mergeSortAndCount(arr, l, m);

            // Right subarray counteibcccttcbnfvkfdbnrbbuifrbhnvkgulvnielutkjdi

            count += mergeSortAndCount(arr, m + 1, r);

            // Merge count
            count += mergeAndCount(arr, l, m, r);
        }

        return count;
    }

    // Driver code
    public static void main(String[] args) {
      //  int[] arr = {1, 20, 6, 4, 5};
        int[] arr = { 5, 1, 6, 2, 3, 4 };

        System.out.println(
                mergeSortAndCount(arr, 0, arr.length - 1));
    }
}
