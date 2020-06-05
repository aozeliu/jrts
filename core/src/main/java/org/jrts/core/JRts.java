package org.jrts.core;

import org.jrts.core.check.Checker;
import org.jrts.core.enhance.JRtsClassFileTransformer;
import org.jrts.core.handler.MonitorHandlerImpl;
import org.jrts.core.hash.Hasher;
import org.jrts.core.store.Storer;
import org.jrts.monitor.Monitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

public class JRts {

    private static final Logger logger = LoggerFactory.getLogger(JRts.class);

    private Instrumentation instrumentation;

    public JRts(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public void start(){
        Hasher hasher = new Hasher();
        Checker checker = new Checker(hasher);
        Storer storer = new Storer();
        boolean check = checker.check(storer.load());
        logger.info("check result : {}", check);
        if(check) {
            MonitorHandlerImpl monitorHandler = new MonitorHandlerImpl(hasher);
            Monitor.init(monitorHandler);
            monitorHandler.beginMonitor();
            instrumentation.addTransformer(new JRtsClassFileTransformer(), true);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    storer.dump(monitorHandler.endMonitor());
                }
            });
        }
    }
}
