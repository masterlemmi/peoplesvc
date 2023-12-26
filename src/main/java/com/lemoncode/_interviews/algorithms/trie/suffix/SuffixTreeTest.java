package com.lemoncode._interviews.algorithms.trie.suffix;

import java.util.List;

public class SuffixTreeTest {
    public static void main(String[] args) {
        SuffixTree suffixTree = new SuffixTree("havanabanana");
        List<String> matches1 = suffixTree.searchText("a");
        matches1.forEach(System.out::println);

        List<String> matches2 = suffixTree.searchText("nab");
        matches2.forEach(System.out::println);

        List<String> matches3 = suffixTree.searchText("nag");
        matches3.forEach(System.out::println);

    }
}
