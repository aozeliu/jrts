package org.example;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceImpl extends BaseService {

    String msg;

    public ServiceImpl(String msg) {
        this.msg = msg;
        log.info("constructor");
    }


    @Override
    public void say() {
        log.info(msg);
    }
}
