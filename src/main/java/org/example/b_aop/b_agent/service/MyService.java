package org.example.b_aop.b_agent.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MyService {
    public void foo() {
        log.info("foo()");
        bar();
    }

    public void bar() {
        log.info("bar()");
    }
}
