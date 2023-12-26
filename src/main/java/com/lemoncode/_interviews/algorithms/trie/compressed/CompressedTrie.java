package com.lemoncode._interviews.algorithms.trie.compressed;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompressedTrie {

    private final Node root = new Node(false);      // Root Node
    private final char CASE;      // 'a' for lower, 'A' for upper

    public CompressedTrie() {
        this('a');
    }

    public CompressedTrie(char caseArg) {
        this.CASE = caseArg;
    }

    // Function to insert a word in
    // the compressed trie
    public void insert(String word) {
        // Store the root
        Node currentNode = root;
        int i = 0;

        // Iterate i less than word
        // length
        while (i < word.length() && currentNode.edgeLabel[word.charAt(i) - CASE] != null) {

            // Find the index
            int index = word.charAt(i) - CASE, j = 0;
            StringBuilder label = currentNode.edgeLabel[index];

            //at what index is new word the same as current word
            while (j < label.length() && i < word.length()
                    && label.charAt(j) == word.charAt(i)) {
                ++i;
                ++j;
            }

            // If is the same as the
            // label length
            if (j == label.length()) {
                //node already exists, we will use that node
                currentNode = currentNode.children[index];
            } else {

                // Inserting a prefix of
                // the existing word
                if (i == word.length()) {
                    Node existingChild
                            = currentNode.children[index];
                    Node newChild = new Node(true);
                    StringBuilder remainingLabel
                            = strCopy(label, j);

                    // Making "facebook"
                    // as "face"
                    label.setLength(j);

                    // New node for "face"
                    currentNode.children[index] = newChild;
                    newChild.children[remainingLabel.charAt(0) - CASE] = existingChild;
                    newChild.edgeLabel[remainingLabel.charAt(0) - CASE] = remainingLabel;
                } else {
                    // Inserting word which has
                    // a partial match with
                    // existing word
                    StringBuilder remainingLabel = strCopy(label, j);

                    Node newChild = new Node(false);
                    StringBuilder remainingWord = strCopy(word, i);

                    // Store the currentNode in
                    // temp node
                    Node temp = currentNode.children[index];

                    label.setLength(j);
                    currentNode.children[index] = newChild;
                    newChild.edgeLabel[remainingLabel.charAt(0) - CASE] = remainingLabel;
                    newChild.children[remainingLabel.charAt(0) - CASE] = temp;
                    newChild.edgeLabel[remainingWord.charAt(0) - CASE] = remainingWord;
                    newChild.children[remainingWord.charAt(0) - CASE] = new Node(true);
                }

                return;
            }
        }

        // Insert new node for new word
        if (i < word.length()) {
            currentNode.edgeLabel[word.charAt(i) - CASE] = strCopy(word, i);
            currentNode.children[word.charAt(i) - CASE] = new Node(true);
        } else {

            // Insert "there" when "therein"
            // and "thereafter" are existing
            currentNode.isEnd = true;
        }
    }

    // Function that creates new String
    // from an existing string starting
    // from the given index
    private StringBuilder strCopy(
            CharSequence str, int index) {
//        StringBuilder result
//                = new StringBuilder(100);
//
//        while (index != str.length()) {
//            result.append(str.charAt(index++));
//        }
//
//        return result;

        return new StringBuilder(str.subSequence(index, str.length()));
    }

    // Function to print the Trie
    public void print() {
        printUtil(root, new StringBuilder());
    }

    // Function to print the word
    // starting from the given node
    private void printUtil(
            Node node, StringBuilder str) {
        if (node.isEnd) {
            System.out.println(str);
        }

        for (int i = 0;
             i < node.edgeLabel.length; ++i) {

            // If edgeLabel is not
            // NULL
            if (node.edgeLabel[i] != null) {
                int length = str.length();

                str = str.append(node.edgeLabel[i]);
                printUtil(node.children[i], str);
                str = str.delete(length, str.length());
            }
        }
    }

    // Function to search a word
    public boolean search(String word) {
        int i = 0;

        // Stores the root
        Node trav = root;

        while (i < word.length()
                && trav.edgeLabel[word.charAt(i) - CASE]
                != null) {
            int index = word.charAt(i) - CASE;
            StringBuilder label = trav.edgeLabel[index];
            int j = 0;

            while (i < word.length()
                    && j < label.length()) {

                // Character mismatch
                if (word.charAt(i) != label.charAt(j)) {
                    return false;
                }

                ++i;
                ++j;
            }

            if (j == label.length() && i <= word.length()) {

                // Traverse further
                trav = trav.children[index];
            } else {

                // Edge label is larger
                // than target word
                // searching for "face"
                // when tree has "facebook"
                return false;
            }
        }

        // Target word fully traversed
        // and current node is word
        return i == word.length() && trav.isEnd;
    }

    // Function to search the prefix
    public boolean startsWith(String prefix) {
        int i = 0;

        // Stores the root
        Node trav = root;

        while (i < prefix.length()
                && trav.edgeLabel[prefix.charAt(i) - CASE]
                != null) {
            int index = prefix.charAt(i) - CASE;
            StringBuilder label = trav.edgeLabel[index];
            int j = 0;

            while (i < prefix.length()
                    && j < label.length()) {

                // Character mismatch
                if (prefix.charAt(i) != label.charAt(j)) {
                    return false;
                }

                ++i;
                ++j;
            }

            if (j == label.length()
                    && i <= prefix.length()) {

                // Traverse further
                trav = trav.children[index];
            } else {

                // Edge label is larger
                // than target word,
                // which is fine
                return true;
            }
        }

        return i == prefix.length();
    }

    public static void main(String[] args) {
        var linesWith30Dists = Arrays.asList(1, 2, 3, 4, 5, 11, 12, 13, 14, 15, 21, 22, 23, 24, 25);
        var distributionIdsByLineNum = new HashMap<String, HashSet<Object>>();
        var distId = 1956;
        for (int i = 0; i < 30; i++) {
            var lineNumber = i + 1;
            var distCount = linesWith30Dists.contains(lineNumber) ? 30 : 1;
            var distList = new HashSet<>();
            for (int j = 0; j < distCount; j++) {
                distList.add((distId++) + "");
            }
            distributionIdsByLineNum.put(String.valueOf(lineNumber), distList);
        }

        IntStream.range(1, 31).forEach( i -> {
            var key = i + "";
            System.out.println(key);
            var es = distributionIdsByLineNum.get(key);
            System.out.println(es.stream().sorted().map(s -> String.valueOf(s)).collect(Collectors.joining(",", "   ", ".")));
        });
    }
}

