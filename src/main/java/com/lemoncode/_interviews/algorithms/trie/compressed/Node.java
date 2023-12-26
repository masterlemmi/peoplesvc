package com.lemoncode._interviews.algorithms.trie.compressed;

// Node class
class Node {

    // Number of symbols
    private final static int SYMBOLS = 26;
    Node[] children = new Node[SYMBOLS];
    StringBuilder[] edgeLabel = new StringBuilder[SYMBOLS];

    boolean isEnd;

    // Function to check if the end
    // of the string is reached
    public Node(boolean isEnd) {
        this.isEnd = isEnd;
    }
}
