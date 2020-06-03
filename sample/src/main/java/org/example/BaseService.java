package org.example;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseService implements IService{



    public void run(){
        log.info("run");
        say();
    }
}
