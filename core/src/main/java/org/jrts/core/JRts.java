package org.jrts.core;

import org.jrts.monitor.Monitor;

import java.lang.instrument.Instrumentation;

public class JRts {

    private Instrumentation instrumentation;

    public JRts(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public void start(){
        Monitor.init(new MonitorHandlerImpl());
        instrumentation.addTransformer(new JRtsClassFileTransformer(), true);
    }
}
