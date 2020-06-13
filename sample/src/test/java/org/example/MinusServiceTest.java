package org.example;

import org.example.service.MinusService;
import org.junit.Assert;
import org.junit.Test;

public class MinusServiceTest {

    @Test
    public void test(){
        MinusService minusService = new MinusService();
        int result = minusService.minus(1, 4);
        Assert.assertEquals(-3, result);
    }

}
