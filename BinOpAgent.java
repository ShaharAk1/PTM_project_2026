package test;

import java.util.function.BinaryOperator;

import test.TopicManagerSingleton.TopicManager;

public class BinOpAgent implements Agent {
    public String agentName = null;
    public String topicInp1 = null;
    public String topicInp2 = null;
    public String topicOutput = null;
    public BinaryOperator<Double> operator;

    double val1 = 0.0 ,val2 = 0.0;

    //CTOR
    public BinOpAgent(String agent, String inp1, String inp2, String out ,BinaryOperator<Double> op) {
        this.agentName = agent;
        this.topicInp1 = inp1;
        this.topicInp2 = inp2;
        this.topicOutput = out;
        this.operator = op;

        TopicManager tm = TopicManagerSingleton.get();
        tm.getTopic(inp1).subscribe(this);
        tm.getTopic(inp2).subscribe(this);
        tm.getTopic(out).addPublisher(this);
    }

    //implementation of Agent

    @Override
    public String getName() {
        return agentName;
    }

    @Override
    public void reset() {
        this.val1 = 0.0;
        this.val2 = 0.0;
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
