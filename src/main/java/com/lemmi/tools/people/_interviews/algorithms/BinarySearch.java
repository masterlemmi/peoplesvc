package com.lemmi.tools.people._interviews.algorithms;

import org.junit.jupiter.api.Assertions;

/* use to quickly find an item in a sorted sequence
1. check middle item
2. if not match, check left array if more, check right array if middle item is less
3. go to 1

 */
public class BinarySearch {

    private int findIndex(String[] array, String findMe) {
        return findIndex(array, 0, array.length, findMe);
    }

    private int findIndex(String[] array, int start, int end, String findMe) {

        int middle = start + (end - start) / 2;
        String midItem = array[middle];

        if (midItem.compareTo(findMe) == 0)
            return middle;
        else if (findMe.compareTo(midItem) < 0) {
            //left half
            return findIndex(array, start, middle - 1, findMe);
        } else {
            //right half
            return findIndex(array, middle + 1, end, findMe);
        }
    }


    public static void main(String[] args) {
        BinarySearch binarySearch = new BinarySearch();
        String[] array = {"b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p"};
        Assertions.assertEquals(6, binarySearch.findIndex(array, "h"));
        Assertions.assertEquals(1, binarySearch.findIndex(array, "c"));
        Assertions.assertEquals(10, binarySearch.findIndex(array, "l"));
        Assertions.assertEquals(13, binarySearch.findIndex(array, "o"));
      //  Assertions.assertEquals(-1, binarySearch.searchAccountsForLettersNotInList(array, "z"));
       // Assertions.assertEquals(-1, binarySearch.searchAccountsForLettersNotInList(array, "a"));
        System.out.println("END");
    }

    private int searchAccountsForLettersNotInList(String[] arr, int start, int end, String find) {
        int midIndex = start + (end - start) / 2;

        if (midIndex >= arr.length || (start == 0 && end == 0)) return -1;

        String midLetter = arr[midIndex];
        System.out.println(midIndex + " - " + midLetter);

        if (midLetter.compareTo(find) == 0) {
            return midIndex;
        } else if (midLetter.compareTo(find) < 0) {
            //look right
            return searchAccountsForLettersNotInList(arr, midIndex + 1, end, find);
        } else if (midLetter.compareTo(find) > 0) {
            //look left
            return searchAccountsForLettersNotInList(arr, start, midIndex, find);
        }

        return -1;
    }

}
