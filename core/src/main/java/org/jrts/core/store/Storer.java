package org.jrts.core.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Storer {

    private static final Logger logger = LoggerFactory.getLogger(Storer.class);

    private File file = new File(".jrts/jrts.dpi");

    public void dump(Map<String, String> map){
        File parent = file.getParentFile();
        if(parent != null && !parent.exists() && !parent.mkdirs()){
            logger.warn("存储依赖信息失败：无法创建依赖信息目录");
        }
        try(PrintWriter writer = new PrintWriter(file)){
            for (Map.Entry<String, String> entry: map.entrySet()) {
                writer.println(entry.getKey() + "=" + entry.getValue());
            }
        }catch (IOException e){
            e.printStackTrace();;
        }
    }

    public Map<String, String> load(){
        if(!file.exists()){
            return null;
        }
        Map<String, String> map = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
            String line = null;
            while((line = reader.readLine()) != null){
                String[] split = line.split("=");
                if(split.length == 2){
                    map.put(split[0], split[1]);
                }else{
                    throw new IllegalStateException("不正确的依赖项: " + line);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return map;
    }

}
