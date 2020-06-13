package org.example.controller;

import org.example.service.MinusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MinusController {

    @Autowired
    private MinusService minusService;

    @RequestMapping("/minus")
    public int minus(@RequestParam("x") int x, @RequestParam("y") int y){
        return minusService.minus(x, y);
    }
}
