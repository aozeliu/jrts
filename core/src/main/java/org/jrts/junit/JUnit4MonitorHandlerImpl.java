package org.jrts.junit;

import lombok.extern.slf4j.Slf4j;
import org.jrts.core.record.Recorder;
import org.jrts.core.select.Selector;
import org.jrts.monitor.junit.JUnit4MonitorHandler;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class JUnit4MonitorHandlerImpl implements JUnit4MonitorHandler {
    private Recorder recorder;
    private Selector selector;

    public JUnit4MonitorHandlerImpl(Recorder recorder, Selector selector) {
        this.recorder = recorder;
        this.selector = selector;
    }

    public static final int RUN = 0;
    public static final int NOT_RUN = 1;

    private Map<String, Boolean> runTests = new ConcurrentHashMap<>();

    @Override
    public int runBefore(Object runner) {
        if(isJUnit4ClassRunner(runner)){
            Class testClass = null;
            try {
                testClass = getTestClass(runner);
                String testClassName = testClass.getName();
                if (!selector.isAffect(testClassName)) {
                    log.info("Test {} is Affected", testClassName);
                    return NOT_RUN;
                }
                runTests.put(testClassName, true);
                recorder.beforeTestRun();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return RUN;
    }

    @Override
    public void runAfter(Object runner) {
        if(isJUnit4ClassRunner(runner)){
            Class testClass = null;
            try {
                testClass = getTestClass(runner);
                String testClassName = testClass.getName();
                Boolean isRun = runTests.remove(testClassName);
                if(isRun != null && isRun) {
                    recorder.afterTestRunWithHashAndStore(testClassName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isJUnit4ClassRunner(Object runner){
        return JUnit4Types.isJUnit4Runner(runner.getClass().getName());
    }

    private Class getTestClass(Object runner) throws Exception{
        Field field = getTestClassField(runner.getClass());
        field.setAccessible(true);
        Object testClass = field.get(runner);
        Field testField = testClass.getClass().getDeclaredField("clazz");
        testField.setAccessible(true);
        return (Class) testField.get(testClass);
    }

    private Field getTestClassField(Class clazz){
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField("testClass");
                return field;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
}
