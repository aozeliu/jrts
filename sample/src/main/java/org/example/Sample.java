package org.example;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sample {

    /**
     * -javaagent:agent/target/jrts-agent-jar-with-dependencies.jar
     */
    public static void main(String[] args) {
        log.info("start");
        Container.service.run();
    }
}
