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
        edges = new List<Node>();
    }

    //Setters & Getters
    public void setName(String name) { this.name = name; }
    public void setEdges(List<Node> newEdges) { this.edges = nnew ArrayList<>(newEdges); }
    public void setMessage(Message msg) { this.msg = msg; }
    
    public String getName() { return this.name; }
    public List<Node> getEdges() { return this.edges; }
    public Message getMessage() { return this.msg; }


    //Add a new edge to list
    public void addEdge(Node newEdge) {
        edges.add(newEdge);
    }

    //Check if the list has a cycle
    public boolean hasCycles(Node curNode , Node compNode) {
        if (edges.isEmpty())
            return false;
        else {
            if (curNode == compNode) //There's a cycle
                return true;
            else if (!hasCycles(curNode.hasPrevious())) //No previous node
                return false;
            else {
                hasCycles(curNode.previous() , compNode); //Check previous
            }

        }
    }


}