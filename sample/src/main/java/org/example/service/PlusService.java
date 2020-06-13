package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlusService {

    public int plus(int x, int y){
        log.info("plus: {} + {}", x, y);
        return x + y;
    }

}
