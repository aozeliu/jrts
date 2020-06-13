package org.example.controller;

import org.example.service.PlusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlusController {

    @Autowired
    private PlusService plusService;

    @RequestMapping("/plus")
    public int plus(@RequestParam("x") int x, @RequestParam("y") int y){
        return plusService.plus(x, y);
    }

}
