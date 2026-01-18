package project_biu.graph;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    public final String name;
    List<Agent> subs = new ArrayList<>(); //subscribers
    List<Agent> pubs = new ArrayList<>(); //publishers

    //CTOR
    Topic(String name) {
        this.name=name;
    }

    //Adds an agent to the subscribers list
    public void subscribe(Agent a){
        subs.add(a);
    }
    //Removes an agent from the subscribers list
    public void unsubscribe(Agent a){
        for(int i=0; i< subs.size(); i++) {
            if (subs.get(i).getName() == a.getName()) {
                subs.remove(i);
                break;
            }    
        }
    }

    //Execute callback() function for all the subscribers.
    public void publish(Message m){
        for(int i=0; i< subs.size(); i++) {
            subs.get(i).callback(this, m);
        }
    }

    //Adds an agent to the publishers list
    public void addPublisher(Agent a){
        pubs.add(a);
    }

    //Removes an agent from the publishers list
    public void removePublisher(Agent a){
        for(int i=0; i< pubs.size(); i++) {
            if (pubs.get(i).getName() == a.getName()) {
                pubs.remove(i);
                break;
            }    
        }
    }


}
