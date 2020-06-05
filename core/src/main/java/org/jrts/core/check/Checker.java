package org.jrts.core.check;

import org.jrts.core.hash.Hasher;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class Checker {

    private Hasher hasher;

    public Checker(Hasher hasher) {
        this.hasher = hasher;
    }



    /**
     *
     * @param map 依赖信息
     * @return true 有依赖信息变动
     */
    public boolean check(Map<String, String> map){
        if(map == null || map.isEmpty()){
            return true;
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String path = entry.getKey();
            String checksum = entry.getValue();
            try {
                String newChecksum = hasher.hash(new URL(path));
                if(!checksum.equals(newChecksum)){
                    return true;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
