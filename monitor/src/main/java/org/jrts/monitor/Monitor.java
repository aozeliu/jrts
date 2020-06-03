package org.jrts.monitor;

/**
 * 监控类，用于记录执行信息
 */
public class Monitor {

    private static MonitorHandler monitorHandler;

    public static void init(final MonitorHandler handler) {
        monitorHandler = handler;
    }

    public static void monitorMethodOnCallBefore(final Class clazz) throws Throwable {
        try {
            if (null != monitorHandler) {
                monitorHandler.handleOnCallBefore(clazz);
            }
        } catch (Throwable cause) {
            handleException(cause);
        }
    }


    public static void monitorMethodOnBefore(final Class methodClass, final Object target) throws Throwable {
        try {
            if (null != monitorHandler) {
                monitorHandler.handleOnBefore(methodClass, target);
            }
        } catch (Throwable cause) {
            handleException(cause);
        }
    }

    public static void monitorMethodOnStaticAccess(Class ownerClass, Class fieldClass) throws Throwable {
        try {
            if (null != monitorHandler) {
                monitorHandler.handleOnStaticAccess(ownerClass, fieldClass);
            }
        } catch (Throwable cause) {
            handleException(cause);
        }
    }

    /**
     * 是否在发生异常时主动对外抛出
     * T:主动对外抛出，会中断方法
     * F:不对外抛出，只将异常信息打印出来
     */
    public static volatile boolean isThrowException = false;
    private static void handleException(Throwable cause) throws Throwable {
        if (isThrowException) {
            throw cause;
        } else {
            cause.printStackTrace();
        }
    }
}
