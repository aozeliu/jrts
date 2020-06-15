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
        boolean isRecord = recorder.record(clazz);
        logger.debug("handleOnCallBefore classname={} isRecord={}",
                clazz.getName(), isRecord);
    }

    @Override
    public void handleOnBefore(Class methodClass, Object target) throws Throwable {
        Class targetClazz = target == null ? null : target.getClass();
        boolean isRecordOfMethod = recorder.record(methodClass);
        boolean isRecordOfTarget = recorder.record(targetClazz);
        logger.debug("handleOnBefore methodClassName={} isRecord={}, targetClassname={}, isRecord={}",
                methodClass.getName(), isRecordOfMethod,
                targetClazz == null ? "null" : targetClazz.getName(), isRecordOfTarget
        );
    }


    @Override
    public void handleOnStaticAccess(Class ownerClass, Class fieldClass) throws Throwable {
        boolean isRecordOfOwner = recorder.record(ownerClass);
        boolean isRecordOfField = recorder.record(fieldClass);
        logger.debug("handleOnStaticAccess ownerClassName={} isRecord={}, fieldClassName={} isRecord={}",
                ownerClass == null ? "null" : ownerClass.getName(), isRecordOfOwner,
                fieldClass == null ? "null" : fieldClass.getName(), isRecordOfField);
    }


}
