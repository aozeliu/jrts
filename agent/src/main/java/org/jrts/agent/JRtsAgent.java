package org.jrts.agent;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.jar.JarFile;

public class JRtsAgent {

    /**
     * 运行时加载
     */
    public static void agentmain(String featureString, Instrumentation inst) {
        load(inst);
    }

    /**
     * 启动时加载
     * @param featureString
     * @param inst
     */
    public static void premain(String featureString, Instrumentation inst) {
        load(inst);
    }

    public static void load(Instrumentation inst){
        try {
            File monitorFile = new File("monitor/target/monitor-1.0-SNAPSHOT.jar");
            inst.appendToBootstrapClassLoaderSearch(
                    new JarFile(monitorFile));
            JRtsClassLoader jRtsClassLoader = new JRtsClassLoader(new URL[]{new File("core/target/core-1.0-SNAPSHOT-jar-with-dependencies.jar").toURI().toURL()}, null);
            Class<?> aClass = jRtsClassLoader.loadClass("org.jrts.core.JRts");
            Constructor<?> constructor = aClass.getConstructor(Instrumentation.class);
            Object jrts = constructor.newInstance(inst);
            aClass.getDeclaredMethod("start").invoke(jrts);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
