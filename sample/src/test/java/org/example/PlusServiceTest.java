package org.example;

import org.example.service.PlusService;
import org.junit.Assert;
import org.junit.Test;

public class PlusServiceTest {

    @Test
    public void test(){
        PlusService plusService = new PlusService();
        int result = plusService.plus(2, 7);
        Assert.assertEquals(9, result);
    }
}
