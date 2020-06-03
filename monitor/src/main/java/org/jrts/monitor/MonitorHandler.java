package org.jrts.monitor;

/**
 * 处理器
 */
public interface MonitorHandler {

    /**
     * 调用之前
     *
     * @param clazz      调用方法时所在的类
     */
    void handleOnCallBefore(Class clazz) throws Throwable;


    /**
     * 执行之前
     *
     * @param methodClass             执行方法所在类
     * @param target                   目标对象
     */
    void handleOnBefore(Class methodClass, Object target) throws Throwable;


    /**
     * 访问静态变量
     *
     * @param ownerClass             静态变量所在类
     * @param fieldClass            静态变量类型
     */
    void handleOnStaticAccess(Class ownerClass, Class fieldClass) throws Throwable;
}