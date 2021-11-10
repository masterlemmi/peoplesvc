package com.lemoncode.relations;

import com.lemoncode.dijkrsta.Node;
import com.lemoncode.dijkrsta.ShortestPath;
import com.lemoncode.dijkrsta.ShortestPathService;
import com.lemoncode.familytree.FamilyTreeMaker;
import com.lemoncode.person.*;
import com.lemoncode.relationship.RelationshipDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Service
@RequiredArgsConstructor
public class ConnectionsService {
    private final PeopleRepository peopleRepository;
    private final PeopleService peopleService;
    private final ConnectionsRepository connectionsRepository;
    private final CacheService cacheService;
    private final ShortestPathService shortestPathService;

    public ConnectionsDTO findConnection(Long sourceId, Long targetId) {
        Person psource = peopleRepository.findByIdNoJoins(sourceId);
        Person target = peopleRepository.findByIdNoJoins(targetId);
        if (psource == null || target == null) {
            throw new PersonNotFoundException("resource/s not found");
        }

        Connections conn = connectionsRepository.find(sourceId, targetId);

        List<Long> shortestPathByIds;
        if (conn == null) {
            ShortestPath shortestPath = shortestPathService.getShortestPath(sourceId, targetId);
            shortestPathByIds = shortestPath.getPath();
            for (Node n : shortestPath.getVisitedNodes()) {
                String path = n.getShortestPath().stream()
                        .map(Node::getPersonId)
                        .map(String::valueOf)
                        .collect(joining(","));
                save(sourceId, n.getPersonId(), path);
            }
        } else {
            String dbShortestPath = conn.getShortestPath();
            if (dbShortestPath == null) return ConnectionsDTO.noLink();
            shortestPathByIds = Arrays.stream(dbShortestPath.split(",")).map(Long::parseLong).collect(Collectors.toList());
        }

        return doit(shortestPathByIds, target);

    }

    private ConnectionsDTO doit(List<Long> shortestPath, Person target) {
        Long targetId = target.getId();
        List<ConnectionsDTO.Node> nodes = new ArrayList<>();
        List<ConnectionsDTO.Edge> links = new ArrayList<>();

        if (CollectionUtils.isEmpty(shortestPath)) {
            return ConnectionsDTO.noLink();
        }

        PersonDTO current = null;
        PersonDTO next;
        for (int i = 0; i < shortestPath.size(); i++) {
            Long currId = shortestPath.get(i);
            Long nextId = i + 1 == shortestPath.size() ? targetId : shortestPath.get(i + 1);
            if (current == null)
                current = peopleService.findOne(currId);

            next = peopleService.findOne(nextId);

            ConnectionsDTO.Node currentNode = new ConnectionsDTO.Node();
            currentNode.setId(currId);
            currentNode.setLabel(current.getFullName());
            nodes.add(currentNode);

            ConnectionsDTO.Edge edge = new ConnectionsDTO.Edge();
            edge.setId(i);
            edge.setSource(currId);
            edge.setTarget(nextId);
            edge.setLabel(whoIsCurrentToNext(currId, next));
            links.add(edge);

            if (i == shortestPath.size() - 1) {
                //last iteration create the targetNode
                ConnectionsDTO.Node targetNode = new ConnectionsDTO.Node();
                targetNode.setId(nextId);
                targetNode.setLabel(next.getFullName());
                nodes.add(targetNode);
            } else {
                current = next;
            }

        }

        ConnectionsDTO connectionsDTO = new ConnectionsDTO();
        connectionsDTO.setNodes(nodes);
        connectionsDTO.setLinks(links);
        Label label = Label.from(links);
        String strLabel = label == Label.DIRECT_ANCESTOR ? Label.ancestorLabel(links) : label.byGender(target.getGender());
        connectionsDTO.setRelationLabel(strLabel);
        connectionsDTO.setStatus("success"); //TODO: set to inprogress for thread based processing

        return connectionsDTO;
    }

    private String whoIsCurrentToNext(Long currId, PersonDTO next) {
        boolean isChild = next.getParents().stream().anyMatch(x -> x.getId().equals(currId));
        if (isChild) return "is parent of";
        boolean isParent = next.getChildren().stream().anyMatch(x -> x.getId().equals(currId));
        if (isParent) return "is child of";

        boolean isSibling = next.getSiblings().stream().anyMatch(x -> x.getId().equals(currId));
        if (isSibling) return "is sibling of";

        for (RelationshipDTO relDTO : next.getRelationships()) {
            if (relDTO.getPeople().stream().anyMatch(x -> x.getId().equals(currId))) {
                return "is " + relDTO.getLabel().toLowerCase() + " of";
            }
        }

        return " undetermined ";

    }

    public void save(Long source, Long target, String path) {
        Person s = peopleRepository.findByIdNoJoins(source);
        Person t = peopleRepository.findByIdNoJoins(target);
        Connections conn = new Connections();
        conn.setShortestPath(path);
        conn.setSource(s);
        conn.setTarget(t);
        connectionsRepository.save(conn);
    }

    //only Families wife/husband/parents/children/
    public ConnectionsDTO findConnection(Long source) {

        ConnectionsDTO cached = cacheService.getConnectionsCache(source);
        if (cached != null) return cached;

        FamilyTreeMaker familyTreeMaker = new FamilyTreeMaker(peopleService);
        familyTreeMaker.start(source);
        ConnectionsDTO connectionsDTO = new ConnectionsDTO();
        connectionsDTO.setNodes(familyTreeMaker.getNodes());
        connectionsDTO.setLinks(familyTreeMaker.getLinks());
        connectionsDTO.setClusters(familyTreeMaker.getClusters());
        connectionsDTO.setRelationLabel(familyTreeMaker.getTreeLabel());
        connectionsDTO.setStatus("success"); //TODO: set to inprogress for thread based processing

        cacheService.addConnectionsCache(source, connectionsDTO);
        return connectionsDTO;
    }
}
