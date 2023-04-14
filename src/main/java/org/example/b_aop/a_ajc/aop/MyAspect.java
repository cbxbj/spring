package org.example.b_aop.a_ajc.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class MyAspect {
    @Before("execution(* org.example.b_aop.a_ajc.service.MyService.foo())")
    public void before() {
        log.info("before()");
    }
}
