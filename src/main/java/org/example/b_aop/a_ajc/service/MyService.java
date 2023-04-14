package org.example.b_aop.a_ajc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MyService {
    public static void foo() {
        log.info("foo()");
    }
}
