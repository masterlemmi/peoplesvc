package com.lemoncode._interviews.algorithms.graph;

import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Djikstra {

    public static void main(String[] args) {
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");
        Node e = new Node("E");

        a.addDest(b, 6);
        a.addDest(d, 1);
        b.addDest(a, 6);
        b.addDest(d, 2);
        b.addDest(e, 2);
        b.addDest(c, 5);
        c.addDest(b, 5);
        c.addDest(e, 5);
        d.addDest(a, 1);
        d.addDest(b, 2);
        d.addDest(e, 1);
        e.addDest(d, 1);
        e.addDest(b, 2);
        e.addDest(c, 5);

        Node source = a;
        source.setDistanceToSource(0);
        Set<Node> visited = new HashSet<>();
        Set<Node> unvisited = new HashSet<>();
        unvisited.add(source);

        while (!unvisited.isEmpty()) {

            Node closestNode = unvisited.stream().min(Comparator.comparing(Node::getDistanceToSource)).orElse(null);
            unvisited.remove(closestNode);
            Map<Node, Integer> adjacentNodeMap = closestNode.getAdjacentNodeMap();

            for (Node node : adjacentNodeMap.keySet()) {
                if (visited.contains(node))
                    continue;

                int currentDistToSrc = closestNode.getDistanceToSource();
                int lweight = adjacentNodeMap.get(node);
                int newDistToSrc = currentDistToSrc + lweight;

                if (newDistToSrc < node.getDistanceToSource()) {
                    node.setDistanceToSource(newDistToSrc);
                    List<Node> shortestPath = closestNode.getShortestPath();
                    List<Node> newShortestPath = new LinkedList<>(shortestPath);
                    newShortestPath.add(node);
                    node.setShortestPath(newShortestPath);
                    unvisited.add(node);
                }
            }


            visited.add(closestNode);
        }

        System.out.println();

        assertResult(visited);
    }

    private static void assertResult(Set<Node> settledNodes) {
        System.out.println();
        Assertions.assertEquals(5, settledNodes.size());
        for (Node node : settledNodes) {
            switch (node.getName()) {
                case "A":
                    Assertions.assertEquals(0, node.getDistanceToSource());
                    Assertions.assertEquals("", getPath(node));
                    break;
                case "B":
                    Assertions.assertEquals(3, node.getDistanceToSource());
                    Assertions.assertEquals("DB", getPath(node));
                    break;
                case "C":
                    Assertions.assertEquals(7, node.getDistanceToSource());
                    Assertions.assertEquals("DEC", getPath(node));
                    break;
                case "D":
                    Assertions.assertEquals(1, node.getDistanceToSource());
                    Assertions.assertEquals("D", getPath(node));
                    break;
                case "E":
                    Assertions.assertEquals(2, node.getDistanceToSource());
                    Assertions.assertEquals("DE", getPath(node));
                    break;
                default:
                    throw new RuntimeException();
            }

        }

        System.out.println("END");

    }

    private static String getPath(Node node) {
        return node.getShortestPath().stream().map(Node::getName).collect(Collectors.joining());
    }


    private static class NodeGraph {
        Set<Node> nodes = new HashSet<>();

        public NodeGraph(Node[] nodeArr) {
            nodes.addAll(Arrays.asList(nodeArr));
        }

    }
}
