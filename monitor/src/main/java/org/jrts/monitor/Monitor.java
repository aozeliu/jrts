package org.jrts.monitor;

/**
 * 监控类，用于记录执行信息
 */
public class Monitor {

    private static MonitorHandler monitorHandler;

    public static void init(final MonitorHandler handler) {
        monitorHandler = handler;
    }

    public static void monitorMethodOnCallBefore(final String classname) throws Throwable {
        try {
            if (null != monitorHandler) {
                monitorHandler.handleOnCallBefore(classname);
            }
        } catch (Throwable cause) {
            handleException(cause);
        }
    }


    public static void monitorMethodOnBefore(final String methodClassName, final Object target) throws Throwable {
        try {
            if (null != monitorHandler) {
                monitorHandler.handleOnBefore(methodClassName, target == null ? "null" : target.getClass().getName());
            }
        } catch (Throwable cause) {
            handleException(cause);
        }
    }

    public static void monitorMethodOnStaticAccess(String ownerClassName, String fieldClassName) throws Throwable {
        try {
            if (null != monitorHandler) {
                monitorHandler.handleOnStaticAccess(ownerClassName, fieldClassName);
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
