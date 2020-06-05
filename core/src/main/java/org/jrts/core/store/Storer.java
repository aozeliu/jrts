package org.jrts.core.store;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.jrts.core.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Storer {

    public List<TestDependency> loadAll(File file){
        File[] files = file.listFiles(
                f -> f.isFile() && f.getPath().endsWith(Constants.DEPENDENCY_FILE_EXTENSION)
        );
        if(files == null){
            return null;
        }
        ArrayList<TestDependency> list = new ArrayList<>(files.length);
        for (File f : files) {
            Map<String, String> data = load(f);
            TestDependency testDependency =
                    new TestDependency(FilenameUtils.getBaseName(f.getPath()), data);
            list.add(testDependency);
        }
        return list;
    }


    public void dump(Map<String, String> map, File file){
        File parent = file.getParentFile();
        if(parent != null && !parent.exists() && !parent.mkdirs()){
            log.warn("存储依赖信息失败：无法创建依赖信息目录");
        }
        try(PrintWriter writer = new PrintWriter(file)){
            for (Map.Entry<String, String> entry: map.entrySet()) {
                writer.println(entry.getKey() + Constants.DEPENDENCY_ENTRY_SPLITTER + entry.getValue());
            }
        }catch (IOException e){
            e.printStackTrace();;
        }
    }

    public Map<String, String> load(File file){
        if(!file.exists()){
            return null;
        }
        Map<String, String> map = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){
            String line = null;
            while((line = reader.readLine()) != null){
                String[] split = line.split(Constants.DEPENDENCY_ENTRY_SPLITTER);
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
