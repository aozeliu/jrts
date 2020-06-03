package org.jrts.core;

import org.jrts.monitor.Monitor;

import java.lang.instrument.Instrumentation;

public class JRts {

    private Instrumentation instrumentation;

    public JRts(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public void start(){
        MonitorHandlerImpl monitorHandler = new MonitorHandlerImpl();
        Monitor.init(monitorHandler);
        instrumentation.addTransformer(new JRtsClassFileTransformer(), true);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                monitorHandler.store();
            }
        });
    }
}
