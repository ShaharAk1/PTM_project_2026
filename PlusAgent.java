package test;

import test.TopicManagerSingleton.TopicManager;

public class PlusAgent implements Agent{
    public double x = 0;
    public double y = 0;

    public PlusAgent(String[] subs, String[] pubs) {
        TopicManager tm = TopicManagerSingleton.get();
        Topic top1 = tm.getTopic(subs[0]);
        Topic top2 = tm.getTopic(subs[1]);
        Topic pubTop = tm.getTopic(pubs[0]);

        Agent agent = new Agent();
        top1.subscribe(agent);
        top2.subscribe(agent);

        //publish x+y
        pubTop.addPublisher(agent);
        if(x instance of double && y instance of double)
            pubTop.publish(x+y);
        else
            throw new
        
    }

    
    //TODO - this part
    //implementation of Agent

    @Override
    public String getName() {
        return agentName;
    }

    @Override
    public void reset() {
        this.x = 0.0;
        this.y = 0.0;
    }

    @Override
    public void callback(String topic, Message msg) {
        //set val1 and val2 to topic message as double
        if (topic.equals(this.topicInp1)) {
            val1 = msg.asDouble;
        } else if (topic.equals(this.topicInp2)) {
            val2 = msg.asDouble;
        }

        //if values are valid save result (after applying lambda operator)
        if(!Double.isNaN(val1) && !Double.isNaN(val2)){
            double result = this.operator.apply(val1, val2);
            //publish to output topic
            TopicManagerSingleton.get().getTopic(topicOutput).publish(new Message(result));
        }

    }

    @Override
    public void close() {

    }
}
