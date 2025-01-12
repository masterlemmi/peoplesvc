package com.lemmi.tools.people._interviews.algorithms.practice;

import org.junit.jupiter.api.Assertions;

public class BinarySearch {

    private int search(String[] array, String findMe) {
        System.out.println(findMe);
        return search(array, 0, array.length, findMe);
    }

    private int search(String[] arr, int start, int end, String find) {
        int midIndex = start + (end - start) / 2;

        if (midIndex >= arr.length || (start == 0 && end == 0)) return -1;

        String midLetter = arr[midIndex];
        System.out.println(midIndex + " - " + midLetter);

        if (midLetter.compareTo(find) == 0) {
            return midIndex;
        } else if (midLetter.compareTo(find) < 0) {
            //look right
            return search(arr, midIndex + 1, end, find);
        } else if (midLetter.compareTo(find) > 0) {
            //look left
            return search(arr, start, midIndex, find);
        }

        return -1;
    }


    public static void main(String[] args) {
        BinarySearch binarySearch = new BinarySearch();
        String[] array = { "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q"};
        Assertions.assertEquals(6, binarySearch.search(array, "h"));
        Assertions.assertEquals(1, binarySearch.search(array, "c"));
        Assertions.assertEquals(10, binarySearch.search(array, "l"));
        Assertions.assertEquals(13, binarySearch.search(array, "o"));
        Assertions.assertEquals(-1, binarySearch.search(array, "z"));
        Assertions.assertEquals(-1, binarySearch.search(array, "a"));
        System.out.println("END");
    }

}
