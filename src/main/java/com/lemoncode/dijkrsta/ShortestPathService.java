package com.lemoncode.dijkrsta;

import com.lemoncode.person.PeopleService;
import com.lemoncode.person.PersonDTO;
import com.lemoncode.person.SimplePersonDTO;
import com.lemoncode.relations.ConnectionsService;
import com.lemoncode.relationship.RelationshipDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

@Service
public class ShortestPathService {

    private static final float WIFE_HUSBAND_DISTANCE = 3f;
    private static final float OTHER_RELATION_DISTANCE=10f;
    private static final float SIBLING_DISTANCE = 1f;
    private static final float CHILD_DISTANCE = 1f;
    private static final float PARENT_DISTANCE = 1.2f;


    @Autowired
    PeopleService peopleService;

    @Autowired
    ConnectionsService connectionsService;

    public List<Long> getShortestPath(Long sourceId, Long targetId) {
        Map<Long, Node> nodeMap = new HashMap<>();
        List<PersonDTO> dtos = peopleService.findAll();
        //map all people to Nodes
        for (PersonDTO dto : dtos) {
            Node node = new Node(dto.getId(), dto.getFullName());
            nodeMap.put(dto.getId(), node);
        }

        //assign adjacent nodes
        for (PersonDTO dto : dtos) {
            Node mainNode = nodeMap.get(dto.getId());

            for (SimplePersonDTO s : dto.getChildren()) {
                Node pNode = nodeMap.get(s.getId());
                mainNode.addDestination(pNode, CHILD_DISTANCE);
            }

            for (SimplePersonDTO s : dto.getSiblings()) {
                Node pNode = nodeMap.get(s.getId());
                mainNode.addDestination(pNode, SIBLING_DISTANCE);
            }

            //children and parents are distance 1
            for (SimplePersonDTO s : dto.getParents()) {
                Node pNode = nodeMap.get(s.getId());
                mainNode.addDestination(pNode, PARENT_DISTANCE);
            }

            //relationships are distance 2 unless WIFE/HUSBAND which is 1.5;
            for (RelationshipDTO rel : dto.getRelationships()) {
                String label = rel.getLabel();

                for (SimplePersonDTO s : rel.getPeople()) {
                    Node pNode = nodeMap.get(s.getId());
                    List<String> closerLabels = Arrays.asList("WIFE", "HUSBAND");
                    float distance = closerLabels.contains(label.toUpperCase()) ? WIFE_HUSBAND_DISTANCE : OTHER_RELATION_DISTANCE;
                    mainNode.addDestination(pNode, distance);
                }
            }
        }

        Graph graph = new Graph();
        for (Map.Entry<Long, Node> entry : nodeMap.entrySet()) {
            graph.addNode(entry.getValue());
        }


        Node sourceNode = nodeMap.get(sourceId);
        graph = Dijkstra.calculateShortestPathFromSource(graph, sourceNode);

        for (Node n : graph.getNodes()) {
            String path = n.getShortestPath().stream()
                    .map(Node::getPersonId)
                    .map(String::valueOf)
                    .collect(joining(","));
            connectionsService.save(sourceNode.getPersonId(), n.getPersonId(), path);
        }

        return nodeMap.get(targetId).getShortestPath().stream()
                .map(Node::getPersonId).collect(toList());

    }
}