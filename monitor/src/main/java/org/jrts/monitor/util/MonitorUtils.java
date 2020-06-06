package org.jrts.monitor.util;

public class MonitorUtils {

    /**
     * 是否在发生异常时主动对外抛出
     * T:主动对外抛出，会中断方法
     * F:不对外抛出，只将异常信息打印出来
     */
    public static volatile boolean isThrowException = false;
    public static void handleException(Throwable cause) throws Throwable {
        if (isThrowException) {
            throw cause;
        } else {
            cause.printStackTrace();
        }
    }

}
