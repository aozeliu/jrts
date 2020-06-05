package org.jrts.core;

import lombok.extern.slf4j.Slf4j;
import org.jrts.core.enhance.JRtsClassFileTransformer;
import org.jrts.core.handler.MonitorHandlerImpl;
import org.jrts.core.hash.Hasher;
import org.jrts.core.store.Storer;
import org.jrts.core.util.Constants;
import org.jrts.monitor.Monitor;

import java.io.File;
import java.lang.instrument.Instrumentation;

@Slf4j
public class JRts {

    private Instrumentation instrumentation;

    public JRts(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public void start(){
        Hasher hasher = new Hasher();
        Storer storer = new Storer();

        MonitorHandlerImpl monitorHandler = new MonitorHandlerImpl(hasher);
        Monitor.init(monitorHandler);
        monitorHandler.beginMonitor();
        instrumentation.addTransformer(new JRtsClassFileTransformer(), true);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                storer.dump(
                        monitorHandler.endMonitor(),
                        new File(
                                Constants.JRTS_DEFAULT_DATA_PATH,
                                JRts.class.getName() + Constants.DEPENDENCY_FILE_EXTENSION)
                );
            }
        });
    }
}
