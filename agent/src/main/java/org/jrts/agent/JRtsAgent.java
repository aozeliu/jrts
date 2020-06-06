package org.jrts.agent;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

public class JRtsAgent {

    public static final String MONITOR_JAR = "monitor.jar";
    public static final String CORE_JAR = "core.jar";
    /**
     * 启动时加载
     *
     * @param featureString
     * @param inst
     */
    public static void premain(String featureString, Instrumentation inst) throws Exception {
        Map<String, String> argMap = parseArgs(featureString);
        String mode = argMap.get("mode");
        String rootDir = argMap.get("root.dir");
        if ("junit4".equals(mode)) {
            load(inst, rootDir, "org.jrts.junit.JUnitJRts");
        } else {
            load(inst, rootDir, "org.jrts.core.SimpleJRts");
        }
    }

    public static void load(Instrumentation instrumentation, String rootDir, String jrtsClassname) throws Exception{
        instrumentation.appendToBootstrapClassLoaderSearch(new JarFile(new File(rootDir, MONITOR_JAR)));
        JRtsClassLoader jRtsClassLoader = new JRtsClassLoader(new URL[]{new File(rootDir, CORE_JAR).toURI().toURL()}, null);
        Class<?> clazz = jRtsClassLoader.loadClass(jrtsClassname);
        start(clazz, instrumentation);
    }

    public static void start(Class clazz, Instrumentation instrumentation) throws Exception{
        Constructor constructor = clazz.getConstructor(Instrumentation.class);
        Object jrts = constructor.newInstance(instrumentation);
        clazz.getDeclaredMethod("start").invoke(jrts);
    }

    /**
     *  逗号分割，等号赋值
     *  f1=v1,f2=v2
     */
    public static Map<String, String> parseArgs(String args){
        Map<String, String> argMap = new HashMap<>();
        if(args != null) {
            String[] split = args.split(",");
            for (String s : split) {
                String[] e = s.split("=");
                if(e.length == 2){
                    argMap.put(e[0], e[1]);
                }else{
                    System.err.println("Arg Error : " + s);
                }
            }
        }
        return argMap;
    }
}
