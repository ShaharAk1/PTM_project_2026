package test;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class GenericConfig implements Config {

    String fileName = "";
    public List<String> lines = new ArrayList<>();
    public List<ParallelAgent> createdAgents = new ArrayList<>();


    public void setConfFile(String path) {
        fileName = path;
    }


    //create the configuration according to the file
    @Override
    public void create() {
        //read from file
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line); //every line is an element in a list
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //continue - if the file's format is correct
        if(lines.size() % 3 == 0){
            ParallelAgent agent;

            for(int i = 0; i < lines.size(); i += 3){
                String className = lines.get(i);
                String[] toSub = lines.get(i+1).split("\\s*,\\s*");       // topics to subscribe
                String[] toPub = lines.get(i+2).split("\\s*,\\s*");     // topics to publish

                //class name - create a new instance of the class.
                try {
                    Class<?> cl = Class.forName(className);
                    Object obj = cl.getDeclaredConstructor(String[].class, String[].class)
                            .newInstance(toSub, toPub);

                    agent = new ParallelAgent(obj);  // עטיפה ב-ParallelAgent
                    createdAgents.add(agent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void close() {
        //close all the agents
        for(ParallelAgent agent: createdAgents){
            //close them here
        }

    }
}
