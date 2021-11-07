package com.lemoncode.dfs;

import com.lemoncode.descendants.DescendantDTO;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class NodeMaker {
    @Getter
    private int index = 0;
    @Getter
    private Map<Long, Node> nodeMapByDescendantId = new HashMap<>();
    private Map<Integer, Node> nodeMapByIndex = new HashMap<>();
    private Graph graph;
    private Set<Integer> allPathIndexes = new HashSet<>();

    private void addToMap(int index, Long descendantId, Node node) {
        nodeMapByDescendantId.put(descendantId, node);
        nodeMapByIndex.put(index, node);
    }

    public Node createParentNode(DescendantDTO main) {
        Node node = new Node(main, index);
        addToMap(index, main.getId(), node);
        index++;
        return node;
    }

    public void createChildNodes(DescendantDTO main) {
        for (DescendantDTO child : main.getChildren()) {
            Node childNode = new Node(child, index);
            addToMap(index, child.getId(), childNode);
            index++;
            createChildNodes(child);
        }
    }

    public void createEdges(DescendantDTO main) {
        int mainIndex = nodeMapByDescendantId.get(main.getId()).getIndex();
        for (DescendantDTO child : main.getChildren()) {
            int childIndex = nodeMapByDescendantId.get(child.getId()).getIndex();
            graph.addEdge(mainIndex, childIndex);
            createEdges(child);
        }
    }


    public void printAllPaths(Long sourceId, Long targetId) {
        int sourceIndex = nodeMapByDescendantId.get(sourceId).getIndex();
        int targetIndex = nodeMapByDescendantId.get(targetId).getIndex();
        graph.printAllPaths(sourceIndex, targetIndex);
        graph.getAllLocalPathList().forEach(path -> {
            allPathIndexes.addAll(path);
            String pathStr = path.stream().map(id -> nodeMapByIndex.get(id))
                    .map(Node::getDto)
                    .map(DescendantDTO::getFullName)
                    .collect(Collectors.joining("-"));
            System.out.println(pathStr);
        });
    }


    public DescendantDTO setExpandMeFieldOnParent(DescendantDTO dto) {
        int nodeIndex = nodeMapByDescendantId.get(dto.getId()).getIndex();
        if (allPathIndexes.contains(nodeIndex)) {
            dto.setExpandMe(true);
            return setExpandMeFieldOnChildren(dto);
        }
        return dto;
    }

    public DescendantDTO setExpandMeFieldOnChildren(DescendantDTO dto) {
        for (DescendantDTO child : dto.getChildren()) {
            int nodeIndex = nodeMapByDescendantId.get(child.getId()).getIndex();
            if (allPathIndexes.contains(nodeIndex)) child.setExpandMe(true);
            setExpandMeFieldOnChildren(child);
        }

        return dto;
    }

    public void createGraph() {
        this.graph = new Graph(index);
        System.out.println("INDEX/VERTICES COUNT: " + index);
    }

}
