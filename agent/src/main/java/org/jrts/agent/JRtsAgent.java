package org.jrts.agent;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.jar.JarFile;

public class JRtsAgent {

    public static final String MONITOR_JAR_PATH = "monitor/target/monitor-1.0-SNAPSHOT.jar";

    /**
     * 启动时加载
     * @param featureString
     * @param inst
     */
    public static void premain(String featureString, Instrumentation inst) throws Exception{
        if(featureString == null) {
            loadSimple(inst);
        }else if(featureString.equals("junit4")){
            loadJUnit4(inst);
        } else{
            loadSimple(inst);
        }
    }

    public static void loadSimple(Instrumentation inst) throws Exception{
        loadCommon(inst);
        JRtsClassLoader jRtsClassLoader = createJRtsClassLoader("core/target/core-1.0-SNAPSHOT-jar-with-dependencies.jar");
        Class<?> clazz = jRtsClassLoader.loadClass("org.jrts.core.SimpleJRts");
        start(clazz, inst);
    }

    public static void loadJUnit4(Instrumentation instrumentation)throws Exception{
        loadCommon(instrumentation);
        JRtsClassLoader jRtsClassLoader = createJRtsClassLoader("junit-agent/target/junit-agent-1.0-SNAPSHOT-jar-with-dependencies.jar");
        Class<?> clazz = jRtsClassLoader.loadClass("org.jrts.junit.JUnitJRts");
        start(clazz, instrumentation);
    }

    public static void loadCommon(Instrumentation instrumentation) throws Exception{
        instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(new File(MONITOR_JAR_PATH)));
    }

    public static JRtsClassLoader createJRtsClassLoader(String jarPath) throws Exception{
        return new JRtsClassLoader(new URL[]{new File(jarPath).toURI().toURL()}, null);
    }

    public static void start(Class clazz, Instrumentation instrumentation) throws Exception{
        Constructor constructor = clazz.getConstructor(Instrumentation.class);
        Object jrts = constructor.newInstance(instrumentation);
        clazz.getDeclaredMethod("start").invoke(jrts);
    }
}
