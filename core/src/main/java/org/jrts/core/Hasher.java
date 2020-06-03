package org.jrts.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.Adler32;

public class Hasher {

    protected static final String ERROR_HASH = "-1";

    private Map<String, String> cache = new ConcurrentHashMap<>();

    public String hash(URL url){
        if(url == null){
            return ERROR_HASH;
        }
        String externalForm = url.toExternalForm();
        String v = cache.get(externalForm);
        if(v != null){
            return v;
        }
        byte[] bytes = loadBytes(url);
        if (bytes == null) {
            return ERROR_HASH;
        }

        Adler32 cksum = new Adler32();
        cksum.update(bytes);
        String ret = Long.toString(cksum.getValue());
        cache.put(externalForm, ret);
        return ret;
    }

    public byte[] loadBytes(URL url){
        try {
            return loadBytes(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] loadBytes(InputStream inputStream){
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int cnt = 0;
        try {
            while((cnt = inputStream.read(buf)) != -1){
                bout.write(buf, 0, cnt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bout.toByteArray();
    }
}
