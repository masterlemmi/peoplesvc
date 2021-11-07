package com.lemoncode.dfs;// JAVA program to print all
// paths from a source to
// destination.
import com.lemoncode.descendants.DescendantDTO;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

// A directed graph using
// adjacency list representation
public class Graph {

    private int vertices;  // No. of vertices in graph
    private ArrayList<Integer>[] adjList;  // adjacency list

    @Getter
    List<List<Integer>> allLocalPathList = new ArrayList<>();

    public static DescendantDTO findPaths(@NonNull DescendantDTO main, Long targetId){
        NodeMaker nodeMaker = new NodeMaker();
        nodeMaker.createParentNode(main);
        nodeMaker.createChildNodes(main);
        nodeMaker.createGraph();
        nodeMaker.createEdges(main);
        nodeMaker.printAllPaths(main.getId(), targetId);
        return nodeMaker.setExpandMeFieldOnParent(main);

    }

    public Graph(int vertices)
    {

        // initialise vertex count
        this.vertices = vertices;

        // initialise adjacency list
        initAdjList();
    }

    // utility method to initialise
    // adjacency list
    @SuppressWarnings("unchecked")
    private void initAdjList()
    {
        adjList = new ArrayList[vertices];

        for (int i = 0; i < vertices; i++) {
            adjList[i] = new ArrayList<>();
        }
    }

    // add edge from u to v
    public void addEdge(int u, int v)
    {
        // Add v to u's list.
        adjList[u].add(v);
    }

    // Prints all paths from
    // 's' to 'd'
    public void printAllPaths(int source, int target)
    {
        boolean[] isVisited = new boolean[vertices];
        ArrayList<Integer> pathList = new ArrayList<>();

        // add source to path[]
        pathList.add(source);

        // Call recursive utility
        printAllPathsUtil(source, target, isVisited, pathList);
    }

    // A recursive function to print
    // all paths from 'u' to 'd'.
    // isVisited[] keeps track of
    // vertices in current path.
    // localPathList<> stores actual
    // vertices in the current path
    private void printAllPathsUtil(Integer u, Integer d,
                                   boolean[] isVisited,
                                   List<Integer> localPathList)
    {

        if (u.equals(d)) {
            allLocalPathList.add(new ArrayList<>(localPathList));
            System.out.println(localPathList);
            // if match found then no need to traverse more till depth
            return;
        }

        // Mark the current node
        isVisited[u] = true;

        // Recur for all the vertices
        // adjacent to current vertex
        for (Integer i : adjList[u]) {
            if (!isVisited[i]) {
                // store current node
                // in path[]
                localPathList.add(i);
                printAllPathsUtil(i, d, isVisited, localPathList);

                // remove current node
                // in path[]
                localPathList.remove(i);
            }
        }

        // Mark the current node
        isVisited[u] = false;
    }

    // Driver program
    public static void main(String[] args)
    {
        // Create a sample graph
        Graph g = new Graph(4);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(0, 3);
        g.addEdge(2, 0);
        g.addEdge(2, 1);
        g.addEdge(1, 3);

        // arbitrary source
        int s = 2;

        // arbitrary destination
        int d = 3;

        System.out.println(
                "Following are all different paths from "
                        + s + " to " + d);
        g.printAllPaths(s, d);
    }
}

// This code is contributed by Himanshu Shekhar.