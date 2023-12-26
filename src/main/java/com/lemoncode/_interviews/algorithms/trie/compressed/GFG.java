package com.lemoncode._interviews.algorithms.trie.compressed;

import org.junit.jupiter.api.Assertions;

class GFG {

    // Driver Code
    public static void main(String[] args) {
        CompressedTrie trie = new CompressedTrie();

        // Insert words
        trie.insert("facebook");
        trie.insert("face");
        trie.insert("facebooktae");
        trie.insert("this");
        trie.insert("there");
        trie.insert("then");

        // Print inserted words
        trie.print();

        // Check if these words
        // are present or not
        Assertions.assertTrue(trie.search("there"));
        Assertions.assertFalse(trie.search("therein"));
        Assertions.assertTrue(trie.startsWith("th"));
        Assertions.assertFalse(trie.startsWith("fab"));
    }
}
