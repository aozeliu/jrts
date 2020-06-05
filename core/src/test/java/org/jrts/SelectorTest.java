package org.jrts;


import org.jrts.core.select.Selector;
import org.jrts.core.util.Constants;
import org.junit.jupiter.api.Test;


import java.util.List;

public class SelectorTest {

    @Test
    public void test(){
        String path = Constants.JRTS_DEFAULT_DATA_PATH;
        List<String> list = new Selector().selectTest(path);
        if(list == null || list.isEmpty()){
            System.out.println("No test is selected");;
        }else{
            System.out.println("Selected Tests : " + list);

        }
    }
}
