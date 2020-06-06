package org.jrts.core;

import lombok.extern.slf4j.Slf4j;
import org.jrts.core.enhance.JRtsClassFileTransformer;
import org.jrts.core.handler.MonitorHandlerImpl;
import org.jrts.core.hash.Hasher;
import org.jrts.core.record.Recorder;
import org.jrts.core.store.Storer;
import org.jrts.monitor.Monitor;

import java.lang.instrument.Instrumentation;

@Slf4j
public class SimpleJRts implements JRts{

    private Instrumentation instrumentation;

    public SimpleJRts(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public void start(){
        Hasher hasher = new Hasher();
        Storer storer = new Storer();

        Recorder recorder = new Recorder(hasher, storer);
        MonitorHandlerImpl monitorHandler = new MonitorHandlerImpl(recorder);
        Monitor.init(monitorHandler);
        recorder.beforeTestRun();
        instrumentation.addTransformer(new JRtsClassFileTransformer(), true);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                recorder.afterTestRun(SimpleJRts.class.getName());
            }
        });
    }
}
