package org.jrts.core.record;

import org.jrts.core.hash.Hasher;
import org.jrts.core.store.Storer;
import org.jrts.core.util.Constants;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Recorder {

    private Map<String, String> hashes = new ConcurrentHashMap<>();
    private Hasher hasher;
    private Storer storer;

    public Recorder(Hasher hasher, Storer storer) {
        this.hasher = hasher;
        this.storer = storer;
    }

    public String record(Class clazz){
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

    public void beforeTestRun(){
        hashes.clear();
    }

    public void afterTestRun(String test){
        storer.dump(
                hashes,
                new File(
                        Constants.JRTS_DEFAULT_DATA_PATH,
                        test + Constants.DEPENDENCY_FILE_EXTENSION
                )
        );
    }


}
