package org.example.b_aop.b_agent.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class MyAspect {
    @Before("execution(* org.example.b_aop.b_agent.service.MyService.*())")
    public void before() {
        log.info("before()");
    }
}
