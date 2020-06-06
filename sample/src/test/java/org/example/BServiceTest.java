package org.example;


import org.junit.Test;

public class BServiceTest {

    @Test
    public void testSay(){
        BService service = new BService("hello jrts BService");
        service.say();
    }

}
