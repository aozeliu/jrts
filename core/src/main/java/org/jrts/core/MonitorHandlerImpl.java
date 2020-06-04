package org.jrts.core;

import org.jrts.monitor.MonitorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MonitorHandlerImpl implements MonitorHandler {

    private static final Logger logger = LoggerFactory.getLogger(MonitorHandlerImpl.class);

    private Hasher hasher;
    private Map<String, String> hashes = new ConcurrentHashMap<>();

    public MonitorHandlerImpl(Hasher hasher) {
        this.hasher = hasher;
    }

    @Override
    public void handleOnCallBefore(Class clazz) throws Throwable {
        String hash = getHash(clazz);
        logger.info("handleOnCallBefore classname={} hashcode={}",
                clazz.getName(), hash);
    }

    @Override
    public void handleOnBefore(Class methodClass, Object target) throws Throwable {
        Class targetClazz = target == null ? null : target.getClass();
        String methodClassHash = getHash(methodClass);
        String targetClassHash = getHash(targetClazz);
        logger.info("handleOnBefore methodClassName={} hashcode={}, targetClassname={}, hashcode={}",
                methodClass.getName(),
                methodClassHash,
                targetClazz == null ? "null" : targetClazz.getName(),
                targetClassHash
        );
    }


    @Override
    public void handleOnStaticAccess(Class ownerClass, Class fieldClass) throws Throwable {
        String ownerClassHash = getHash(ownerClass);
        String fieldClassHash = getHash(fieldClass);
        logger.info("handleOnStaticAccess ownerClassName={} hashcode={}, fieldClassName={} hashcode={}",
                ownerClass == null ? "null" : ownerClass.getName(),
                ownerClassHash,
                fieldClass == null ? "null" : fieldClass.getName(),
                fieldClassHash);
    }

    public URL getUrlForClass(Class clazz){
        String fileName = clazz.getSimpleName().concat(".class");
        return clazz.getResource(fileName);
    }

    public String getHash(Class clazz){
        if(clazz == null){
            return hasher.hash(null);
        }
        URL url = getUrlForClass(clazz);
        if(url == null){
            return hasher.hash(null);
        }
        String externalForm = url.toExternalForm();
        String hash = hashes.get(externalForm);
        if(hash == null) {
            hash = hasher.hash(url);
            hashes.put(externalForm, hash);
        }
        return hash;
    }

    public void beginMonitor(){
        hashes.clear();
    }

    public Map<String, String> endMonitor(){
        return hashes;
    }
}
