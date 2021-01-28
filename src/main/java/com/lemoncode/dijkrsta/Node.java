package com.lemoncode.dijkrsta;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Node {

    @EqualsAndHashCode.Include
    private Long personId;
    private String name;

    private LinkedList<Node> shortestPath = new LinkedList<>();

    private Float distance = Float.MAX_VALUE;

    private Map<Node, Float> adjacentNodes = new HashMap<>();

    public Node(Long personId, String name) {
        this.personId = personId;
        this.name = name;
    }


    public void addDestination(Node destination, float distance) {
        Float currDistance = adjacentNodes.get(destination);
        if (currDistance != null && currDistance < distance) {
            adjacentNodes.put(destination, currDistance);
        } else {
            adjacentNodes.put(destination, distance);
        }

    }


}