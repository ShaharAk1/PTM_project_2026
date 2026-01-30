package test;

import test.TopicManagerSingleton.TopicManager;

public class PlusAgent implements Agent {
    double x;
    double y;
    boolean hasX = false;
    boolean hasY = false;
    Topic pubTop;
    String topicX = "" + 0;
    String topicY= "" + 0;

    public PlusAgent(String[] subs, String[] pubs) {
        TopicManager tm = TopicManagerSingleton.get();

        topicX = subs[0];
        topicY = subs[1];

        Topic top1 = tm.getTopic(topicX);
        Topic top2 = tm.getTopic(topicY);
        pubTop = tm.getTopic(pubs[0]);

        top1.subscribe(this);
        top2.subscribe(this);

        pubTop.addPublisher(this);
    }

    @Override
    public void callback(String topic, Message msg) {

        double value = msg.asDouble; //get double value of the message

        if (topic.equals(topicX)) {
            x = value;
            hasX = true;
        } else if (topic.equals(topicY)) {
            y = value;
            hasY = true;
        }

        if (hasX && hasY) {
            pubTop.publish(new Message(x + y));
        }
    }
    @Override
    public String getName() {
        return "PlusAgent";
    }

    @Override
    public void reset() {
        hasX = false;
        hasY = false;
        x = 0;
        y = 0;
    }

    @Override
    public void close() {
        reset();
    }
}
