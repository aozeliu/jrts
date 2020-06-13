package org.jrts.junit;

public class JUnit4Types {

    public static boolean isJUnit4Runner(String classname){
        if(classname == null){
            return false;
        }
        return classname.equals("org.junit.runners.ParentRunner")
                || classname.equals("org.junit.runners.BlockJUnit4ClassRunner")
                || classname.equals("org.springframework.test.context.junit4.SpringRunner")
                || classname.equals("org.springframework.test.context.junit4.SpringJUnit4ClassRunner");
    }
}
