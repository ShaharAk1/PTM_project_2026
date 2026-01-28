package test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import test.TopicManagerSingleton.TopicManager;

public class Graph extends ArrayList<Node>{

    public boolean hasCycles() {
        for (Node n : this) { //this = the arrayList
            if (n.hasCycles()) {
                return true;
            }
        }
        return false;
    }
    public void createFromTopics() {
        this.clear();
        TopicManager tm = TopicManagerSingleton.get();

        //temporary map- to not create the same node twice
        Map<String, Node> nodes = new HashMap<>();

        for (Topic t : tm.getTopics()) {
            //create or get topic's edge (with T prefix)
            String tName = "T" + t.name;
            if (!nodes.containsKey(tName)) {
                nodes.put(tName, new Node(tName));
            }
            Node tNode = nodes.get(tName);

            //for each agent in the topic's subs create an edge from topic to agent (A prefix)
            for (Agent a : t.subs) {
                String aName = "A" + a.getName();
                if (!nodes.containsKey(aName)) {
                    nodes.put(aName, new Node(aName));
                }
                Node aNode = nodes.get(aName);
                tNode.addEdge(aNode); // Topic -> Agent
            }

            //for each pub of the topic create an edge from the agent to the topic (A prefix)
            for (Agent a : t.pubs) {
                String aName = "A" + a.getName();
                if (!nodes.containsKey(aName)) {
                    nodes.put(aName, new Node(aName));
                }
                Node aNode = nodes.get(aName);
                aNode.addEdge(tNode); // Agent -> Topic
            }
        }

        //Add everything to the graph's arrayList
        this.addAll(nodes.values());
    }
    
}
