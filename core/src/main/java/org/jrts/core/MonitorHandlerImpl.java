package org.jrts.core;

import org.jrts.monitor.MonitorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonitorHandlerImpl implements MonitorHandler {

    private static final Logger logger = LoggerFactory.getLogger(MonitorHandlerImpl.class);

    @Override
    public void handleOnCallBefore(Class clazz) throws Throwable {
        logger.info("handleOnCallBefore classname={}", clazz.getName());
    }

    @Override
    public void handleOnBefore(Class methodClass, Object target) throws Throwable {
        logger.info("handleOnBefore methodClassName={}, targetClassname={}",
                methodClass.getName(), target == null ? "null" : target.getClass().getName());
    }


    @Override
    public void handleOnStaticAccess(Class ownerClass, Class fieldClass) throws Throwable {
        logger.info("handleOnStaticAccess ownerClassName={}, fieldClassName={}",
                ownerClass == null ? "null" : ownerClass.getName(),
                fieldClass == null ? "null" : fieldClass.getName());
    }
}
