package com.lemoncode.relations;

import com.lemoncode.dijkrsta.ShortestPathService;
import com.lemoncode.person.*;
import com.lemoncode.relationship.RelationshipDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConnectionsService {
    @Autowired
    PeopleRepository peopleRepository;

    @Autowired
    PeopleService peopleService;

    @Autowired
    ConnectionsRepository connectionsRepository;

    @Autowired
    ShortestPathService shortestPathService;

    public ConnectionsDTO findConnection(Long sourceId, Long targetId) {
        Person psource = peopleRepository.findByIdNoJoins(sourceId);
        Person target = peopleRepository.findByIdNoJoins(targetId);
        if (psource == null || target == null) {
            throw new PersonNotFoundException("resource/s not found");
        }

        Connections conn = connectionsRepository.find(sourceId, targetId);

        List<Long> shortestPath;
        if (conn == null) {
            shortestPath = shortestPathService.getShortestPath(sourceId, targetId);
        } else {
            String dbShortestPath = conn.getShortestPath();
            if (dbShortestPath == null) return ConnectionsDTO.noLink();
            shortestPath = Arrays.stream(dbShortestPath.split(",")).map(Long::parseLong).collect(Collectors.toList());
        }

        return doit(shortestPath, target);

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

            if (i == shortestPath.size() -1){
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
        connectionsDTO.setRelationLabel(Label.from(links).byGender(target.getGender()));
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

    @Transactional
    public void deleteAll() {
      int deleted =  connectionsRepository.deleteAll();
        System.out.println("Deleted " + deleted + " rows");
    }



}
