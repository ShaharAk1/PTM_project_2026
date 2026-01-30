package test;

import test.TopicManagerSingleton.TopicManager;

public class IncAgent implements Agent {
    double x;
    boolean hasX = false;
    Topic pubTop;
    String topicX = "" + 0;


    public IncAgent(String[] subs, String[] pubs) {
        TopicManager tm = TopicManagerSingleton.get();

        topicX = subs[0];

        Topic top1 = tm.getTopic(topicX);
        pubTop = tm.getTopic(pubs[0]);

        top1.subscribe(this);

        pubTop.addPublisher(this);
    }

    @Override
    public void callback(String topic, Message msg) {

        double value = msg.asDouble; //get double value of the message

        if (topic.equals(topicX)) {
            x = value;
            hasX = true;
        }

        if (hasX) {
            pubTop.publish(new Message(x+1)); //increment the message by 1
        }
    }
    @Override
    public String getName() {
        return "IncAgent";
    }

    @Override
    public void reset() {
        hasX = false;
        x = 0;
    }

    @Override
    public void close() {
        reset();
    }
}
