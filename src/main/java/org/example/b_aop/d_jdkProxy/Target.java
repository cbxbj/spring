package org.example.b_aop.d_jdkProxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Target implements Foo {

    @Override
    public void foo() {
        log.info("target foo()");
    }

    @Override
    public int bar() {
        log.info("target bar()");
        return 100;
    }

}