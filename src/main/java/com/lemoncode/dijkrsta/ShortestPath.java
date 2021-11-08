package com.lemoncode.dijkrsta;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ShortestPath {
    private List<Long> path;
    private Set<Node> visitedNodes;
}
