package org.jrts.core.check;

import org.jrts.core.hash.Hasher;
import org.jrts.core.store.TestDependency;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    public boolean check(TestDependency testDependency){
        return check(testDependency.getDependencies());
    }

    /**
     * 检查哪些测试被影响了
     * @param list 测试及其依赖信息
     * @return 被影响的测试名称列表
     */
    public List<String> checkAffected(List<TestDependency> list){
        if(list == null){
            return null;
        }
        ArrayList<String> affected = new ArrayList<>();
        list.forEach(t -> {
            if(check(t)){
                affected.add(t.getTest());
            }
        });
        return affected;
    }

    /**
     * 检查哪些测试没有被影响
     * @param list 测试及其依赖信息
     * @return 没被影响的测试名称列表
     */
    public List<String> checkNonAffected(List<TestDependency> list){
        if(list == null){
            return null;
        }
        ArrayList<String> nonAffected = new ArrayList<>();
        list.forEach(t -> {
            if(!check(t)){
                nonAffected.add(t.getTest());
            }
        });
        return nonAffected;
    }

}
