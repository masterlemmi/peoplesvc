package com.lemmi.tools.people._interviews.algorithms.trie.suffix;

public class SuffixTreePrinter {


    public static String print(Node node) {
        return printTree(node, "");
    }


    private static String printTree(Node node, String depthIndicator) {
        StringBuilder str = new StringBuilder();
        String positionStr = node.getPosition() > -1 ? "[" + String.valueOf(node.getPosition()) + "]" : "";
        str.append(depthIndicator).append(node.getText()).append(positionStr).append("\n");

        for (Node child : node.getChildren()) {
            str.append(SuffixTreePrinter.printTree(child, depthIndicator + "\t"));
        }
        return str.toString();
    }


}
