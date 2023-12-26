package com.lemoncode._interviews.algorithms.graph.djikstra;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class  Node {
    private final String name;
    Integer distanceToSource = Integer.MAX_VALUE;
    Map<Node, Integer> adjacentNodeMap = new HashMap<>();
    List<Node> shortestPath = new LinkedList<>();

    public void addDest(Node b, int i) {
        adjacentNodeMap.put(b, i);
    }

}