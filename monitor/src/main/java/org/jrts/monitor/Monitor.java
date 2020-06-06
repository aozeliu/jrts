package org.jrts.monitor;


import org.jrts.monitor.util.MonitorUtils;

/**
 * 监控类，用于记录执行信息
 */
public class Monitor {

    private static MonitorHandler monitorHandler;

    public static void init(MonitorHandler handler){
        monitorHandler = handler;
    }

    public static void monitorMethodOnCallBefore(final Class clazz) throws Throwable {
        try {
            if (null != monitorHandler) {
                monitorHandler.handleOnCallBefore(clazz);
            }
        } catch (Throwable cause) {
            MonitorUtils.handleException(cause);
        }
    }


    public static void monitorMethodOnBefore(final Class methodClass, final Object target) throws Throwable {
        try {
            if (null != monitorHandler) {
                monitorHandler.handleOnBefore(methodClass, target);
            }
        } catch (Throwable cause) {
            MonitorUtils.handleException(cause);
        }
    }

    public static void monitorMethodOnStaticAccess(Class ownerClass, Class fieldClass) throws Throwable {
        try {
            if (null != monitorHandler) {
                monitorHandler.handleOnStaticAccess(ownerClass, fieldClass);
            }
        } catch (Throwable cause) {
            MonitorUtils.handleException(cause);
        }
    }
}
