package test;

import java.util.HashMap;
import java.util.Collection;

public class TopicManagerSingleton {

    public static class TopicManager {
        private static final TopicManager instance = new TopicManager();
        HashMap<String, Topic> topics = new HashMap<>();

        //private CTOR 
        private TopicManager() {
        }

        //returns the name's corresponding topic using a hash map. If it doesn't exist - create it in the hashmap.
        public Topic getTopic(String name) {
            //if name doesn't exist - create it and insert into hashmap
            if(!topics.containsKey(name)) {
                Topic newTopic = new Topic(name);
                topics.put(name, newTopic);
            }
            return topics.get(name);
        }

        //returns a collection of all the topics in the hashmap.
        public Collection<Topic> getTopics() {
           return topics.values();
        }

        //clears the map
        public void clear() {
            topics.clear();
        }
    }

    public static TopicManager get() {
        return TopicManager.instance;
    }
    
}
