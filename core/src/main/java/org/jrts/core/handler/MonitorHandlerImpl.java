package org.jrts.core.handler;

import org.jrts.core.record.Recorder;
import org.jrts.monitor.MonitorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MonitorHandlerImpl implements MonitorHandler {

    private static final Logger logger = LoggerFactory.getLogger(MonitorHandlerImpl.class);

    private Recorder recorder;


    public MonitorHandlerImpl(Recorder recorder) {
        this.recorder = recorder;
    }

    @Override
    public void handleOnCallBefore(Class clazz) throws Throwable {
        String hash = recorder.record(clazz);
        logger.debug("handleOnCallBefore classname={} hashcode={}",
                clazz.getName(), hash);
    }

    @Override
    public void handleOnBefore(Class methodClass, Object target) throws Throwable {
        Class targetClazz = target == null ? null : target.getClass();
        String methodClassHash = recorder.record(methodClass);
        String targetClassHash = recorder.record(targetClazz);
        logger.debug("handleOnBefore methodClassName={} hashcode={}, targetClassname={}, hashcode={}",
                methodClass.getName(),
                methodClassHash,
                targetClazz == null ? "null" : targetClazz.getName(),
                targetClassHash
        );
    }


    @Override
    public void handleOnStaticAccess(Class ownerClass, Class fieldClass) throws Throwable {
        String ownerClassHash = recorder.record(ownerClass);
        String fieldClassHash = recorder.record(fieldClass);
        logger.debug("handleOnStaticAccess ownerClassName={} hashcode={}, fieldClassName={} hashcode={}",
                ownerClass == null ? "null" : ownerClass.getName(),
                ownerClassHash,
                fieldClass == null ? "null" : fieldClass.getName(),
                fieldClassHash);
    }


}
