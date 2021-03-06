package com.lemoncode.familytree;

import com.lemoncode.person.PeopleService;
import com.lemoncode.person.PersonDTO;
import com.lemoncode.person.SimplePersonDTO;
import com.lemoncode.relations.ConnectionsDTO;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;


public class FamilyTreeMakerAll {

    PeopleService peopleService;
    //Map<Long, ConnectionsDTO.Node> nodeMap = new HashMap<>();
    List<Long> doneList = new ArrayList<>();
    List<ConnectionsDTO.Edge> links = new ArrayList<>();
    Set<ConnectionsDTO.Node> nodes = new HashSet<>();
    List<ConnectionsDTO.Cluster> clusters = new ArrayList<>();

    int edgeIndex = 0;

    public FamilyTreeMakerAll(PeopleService peopleService) {
        this.peopleService = peopleService;
    }


    public void generate(Long id) {

        if (doneList.contains(id)) return;

        PersonDTO main = peopleService.findOne(id);
        nodes.add(toNode(main));
        List<Long> toIterate = new ArrayList<>();
        Set<SimplePersonDTO> parents = main.getParents();
        for (SimplePersonDTO parent : parents) {
            nodes.add(toNode(parent));
            links.add(toEdge(main.getId(), parent.getId(), "is child of"));
            toIterate.add(parent.getId());
        }
        Set<SimplePersonDTO> children = main.getChildren();
        for (SimplePersonDTO child : children) {
            nodes.add(toNode(child));
            links.add(toEdge(child.getId(), main.getId(), "is child of"));
            toIterate.add(child.getId());
        }
        Set<SimplePersonDTO> asawas = main.getRelationships().stream().filter(rel ->
                rel.getLabel().equalsIgnoreCase("wife")
                        || rel.getLabel().equalsIgnoreCase("husband")
        ).flatMap(x -> x.getPeople().stream()).collect(toSet());
        for (SimplePersonDTO asawa : asawas) {
            nodes.add(toNode(asawa));
            links.add(toEdge(main.getId(), asawa.getId(), " -- "));
            toIterate.add(asawa.getId());
        }

        if (!alreadyInExistingCluster(main.getId()) && !CollectionUtils.isEmpty(main.getSiblings())) {
            ConnectionsDTO.Cluster c = new ConnectionsDTO.Cluster();
            Set<SimplePersonDTO> sibs = main.getSiblings();
            StringBuilder label = new StringBuilder();
            for (SimplePersonDTO s : sibs) {
                c.addChild(s.getId().toString());
                label.append(s.getId());
            }
            c.addChild(main.getId().toString());
            label.append(main.getId().toString());
            c.setLabel(label.toString());
            c.setId(label.toString());
            this.clusters.add(c);
        }

        doneList.add(main.getId());

        toIterate.forEach(this::generate);

    }

    public ConnectionsDTO.Edge toEdge(Long currId, Long nextId, String label) {
        ConnectionsDTO.Edge edge = new ConnectionsDTO.Edge();
        edge.setId(++edgeIndex);
        edge.setSource(currId);
        edge.setTarget(nextId);
        edge.setLabel(label);
        return edge;
    }

    public ConnectionsDTO.Node toNode(PersonDTO dto) {
        ConnectionsDTO.Node currentNode = new ConnectionsDTO.Node();
        currentNode.setId(dto.getId());
        currentNode.setLabel(dto.getFullName());
        return currentNode;
    }

    public ConnectionsDTO.Node toNode(SimplePersonDTO dto) {
        ConnectionsDTO.Node currentNode = new ConnectionsDTO.Node();
        currentNode.setId(dto.getId());
        currentNode.setLabel(dto.getFullName());
        return currentNode;
    }

    public List<ConnectionsDTO.Node> getNodes() {
        return new ArrayList<>(nodes);
    }

    public List<ConnectionsDTO.Edge> getLinks() {
        return this.links;
    }

    public List<ConnectionsDTO.Cluster> getClusters() {
        return this.clusters;
    }

    private boolean alreadyInExistingCluster(Long l) {
        return this.clusters.stream()
                .flatMap(x -> x.getChildNodeIds().stream())
                .anyMatch(s -> s.equalsIgnoreCase(l.toString()));

    }
}