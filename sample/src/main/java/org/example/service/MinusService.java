package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MinusService {

    public int minus(int x, int y){
        log.info("minus : {} - {}", x, y);
        return  x - y;
    }
}
