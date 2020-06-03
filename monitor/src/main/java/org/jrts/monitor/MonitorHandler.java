package org.jrts.monitor;

/**
 * 处理器
 */
public interface MonitorHandler {

    /**
     * 调用之前
     *
     * @param classname      调用方法时所在的类
     */
    void handleOnCallBefore(String classname) throws Throwable;


    /**
     * 执行之前
     *
     * @param methodClassName             执行方法所在类名
     * @param targetClassname                    目标对象实例
     */
    void handleOnBefore(String methodClassName, String targetClassname) throws Throwable;


    /**
     * 访问静态变量
     *
     * @param ownerClassName             静态变量所在类
     * @param fieldClassName            静态变量类型
     */
    void handleOnStaticAccess(String ownerClassName, String fieldClassName) throws Throwable;
}