package org.jrts.junit;

import org.jrts.core.JRts;
import org.jrts.core.enhance.JRtsClassFileTransformer;
import org.jrts.core.handler.MonitorHandlerImpl;
import org.jrts.core.hash.Hasher;
import org.jrts.core.record.Recorder;
import org.jrts.core.select.Selector;
import org.jrts.core.store.Storer;
import org.jrts.monitor.Monitor;
import org.jrts.monitor.junit.JUnit4Monitor;

import java.lang.instrument.Instrumentation;


public class JUnitJRts implements JRts {

    private Instrumentation instrumentation;

    public JUnitJRts(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public void start(){
        Storer storer = new Storer();
        Hasher hasher = new Hasher();
        Recorder recorder = new Recorder(hasher, storer);
        Selector selector = new Selector();
        MonitorHandlerImpl monitorHandler = new MonitorHandlerImpl(recorder);
        Monitor.init(monitorHandler);
        JUnit4MonitorHandlerImpl jUnit4MonitorHandlerImpl = new JUnit4MonitorHandlerImpl(recorder, selector);
        JUnit4Monitor.init(jUnit4MonitorHandlerImpl);
        instrumentation.addTransformer(new JUnit4ClassFileTransformer());
        instrumentation.addTransformer(new JRtsClassFileTransformer());
    }

}
