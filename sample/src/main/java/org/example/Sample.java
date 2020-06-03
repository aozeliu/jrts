package org.example;

public class Sample {

    /**
     * -javaagent:agent/target/jrts-agent-jar-with-dependencies.jar
     */
    public static void main(String[] args) {
        System.out.println("start");
        Container.service.run();
    }
}
