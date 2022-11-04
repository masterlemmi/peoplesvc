package com.lemoncode._interviews.algorithms;

import org.junit.jupiter.api.Assertions;

public class BinarySearch {

    private int search(String[] array, String findMe) {
        return search(array, 0, array.length, findMe);
    }

    private int search(String[] array, int start, int end, String findMe) {

        int middle = start + (end - start) / 2;
        String midItem = array[middle];

        if (midItem.compareTo(findMe) == 0)
            return middle;
        else if (findMe.compareTo(midItem) < 0) {
            //left half
            return search(array, start, middle - 1, findMe);
        } else {
            //right half
            return search(array, middle + 1, end, findMe);
        }
    }


    public static void main(String[] args) {
        BinarySearch binarySearch = new BinarySearch();
        String[] array = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p"};
        Assertions.assertEquals(7, binarySearch.search(array, "h"));
        Assertions.assertEquals(2, binarySearch.search(array, "c"));
        Assertions.assertEquals(11, binarySearch.search(array, "l"));
        Assertions.assertEquals(14, binarySearch.search(array, "o"));
        System.out.println("END");
    }

}
