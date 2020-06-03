package org.jrts.core;

import org.jrts.monitor.MonitorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorHandlerImpl implements MonitorHandler {

    private static final Logger logger = LoggerFactory.getLogger(MonitorHandlerImpl.class);

    @Override
    public void handleOnCallBefore(String classname) throws Throwable {
        logger.info("handleOnCallBefore classname={}", classname);
    }

    @Override
    public void handleOnBefore(String methodClassName, String targetClassname) throws Throwable {
        logger.info("handleOnBefore methodClassName={}, targetClassname={}",
                methodClassName, targetClassname);
    }


    @Override
    public void handleOnStaticAccess(String ownerClassName, String fieldClassName) throws Throwable {
        logger.info("handleOnStaticAccess ownerClassName={}, fieldClassName={}", ownerClassName, fieldClassName);
    }
}
