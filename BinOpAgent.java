package test;

import java.util.function.BinaryOperator;

import test.TopicManagerSingleton.TopicManager;

public class BinOpAgent{
    public String agentName;
    public String topicInp1;
    public String topicInp2;
    public String topicOutput;

    public BinOpAgent(String agent, String inp1, String inp2, String out) {
        this.agentName = agent;
        this.topicInp1 = inp1;
        this.topicInp2 = inp2;
        this.topicOutput = out;
        
    }
}
