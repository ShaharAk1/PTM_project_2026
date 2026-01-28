package test;

import java.util.ArrayList;
import java.util.List;


public class Node {
    private String name;
    private List<Node> edges;
    private Message msg;

    //CTOR
    public Node (String name) {
        this.name = name;
        edges = new ArrayList<Node>();
    }

    //Setters & Getters
    public void setName(String name) { this.name = name; }
    public void setEdges(List<Node> newEdges) { this.edges = new ArrayList<Node>(newEdges); }
    public void setMessage(Message msg) { this.msg = msg; }
    
    public String getName() { return this.name; }
    public List<Node> getEdges() { return this.edges; }
    public Message getMessage() { return this.msg; }


    //Add a new edge to list
    public void addEdge(Node newEdge) {
        edges.add(newEdge);
    }

    //Check if the list has a cycle
    public boolean hasCycles() {
        return hasCycles(new ArrayList<Node>());
    }

    public boolean hasCycles(List<Node> path) {
        if (path.contains(this)) {
            return true; //cycle
        }

        path.add(this); //add this node to path

        for (Node neighbor : edges) {
            //create new version of path to not affect other nodes
            if (neighbor.hasCycles(new ArrayList<>(path))) {
                return true;
            }
        }
        path.remove(this);
        return false;
    }


}