package org.example;

import org.junit.Test;

public class AServiceTest {

    @Test
    public void test(){
        AService service = new AService("hello jrts AService");
        service.say();
    }
}
