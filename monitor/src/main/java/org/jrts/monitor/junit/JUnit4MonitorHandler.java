package org.jrts.monitor.junit;

public interface JUnit4MonitorHandler {

    int runBefore(Object runner);
    void runAfter(Object runner);
}
