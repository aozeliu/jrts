package org.jrts.core.record;

import org.jrts.core.hash.Hasher;
import org.jrts.core.store.Storer;
import org.jrts.core.util.Constants;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Recorder {

    private Map<Class, Boolean> classes = new ConcurrentHashMap<>();
    private Map<String, Boolean> urls = new ConcurrentHashMap<>();
    private Hasher hasher;
    private Storer storer;

    public Recorder(Hasher hasher, Storer storer) {
        this.hasher = hasher;
        this.storer = storer;
    }

    public boolean record(File file){
        try {
            URL url = file.toURI().toURL();
            return recordUrl(url.toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean record(Class clazz){
        if(clazz == null){
            return false;
        }

        Boolean previous = classes.putIfAbsent(clazz, Boolean.TRUE);
        if(previous != Boolean.TRUE) {
            URL url = getUrlForClass(clazz);
            if (url == null) {
                return false;
            }
            String externalForm = url.toExternalForm();
            return recordUrl(externalForm);
        }
        return false;
    }


    private URL getUrlForClass(Class clazz){
        String classname = clazz.getName();
        int index = classname.lastIndexOf('.');
        String fileName = clazz.getSimpleName();
        if(index != -1){
            fileName = classname.substring(index+1);
        }
        fileName = fileName.concat(".class");
        return clazz.getResource(fileName);
    }

    public List<String> getUrls(){
        Set<String> keySet = urls.keySet();
        return new ArrayList<>(keySet);
    }

    public void beforeTestRun(){
        classes.clear();
        urls.clear();
    }


    private boolean recordUrl(String url){
        return urls.putIfAbsent(url, Boolean.TRUE) == Boolean.TRUE;
    }

    public void afterTestRunWithHashAndStore(String test){
        List<String> urls = getUrls();
        storer.dump(
                hasher.hash(urls),
                new File(
                        Constants.JRTS_DEFAULT_DATA_PATH,
                        test + Constants.DEPENDENCY_FILE_EXTENSION
                )
        );
    }


}
