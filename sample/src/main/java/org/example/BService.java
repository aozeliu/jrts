package org.example;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BService implements IService {

    String msg;

    public BService(String msg) {
        this.msg = msg;
        log.info("BService constructor");
    }


    @Override
    public void say() {
        log.info(msg + " B");
    }
}
