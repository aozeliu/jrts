package org.jrts.monitor.junit;

public class JUnit4Monitor {

    private static JUnit4MonitorHandler handler;

    public static void init(JUnit4MonitorHandler handler){
        JUnit4Monitor.handler = handler;
    }

    public static int runBefore(Object runner){
        return handler.runBefore(runner);
    }

    public static void runAfter(Object runner){
        handler.runAfter(runner);
    }

}
