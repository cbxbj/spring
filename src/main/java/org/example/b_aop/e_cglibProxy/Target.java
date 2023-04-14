package org.example.b_aop.e_cglibProxy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Target {
    public void save() {
        log.info("save()");
    }

    public void save(int i) {
        log.info("save(int i),i:{}", i);
    }

    public void save(long j) {
        log.info("save(long j),j:{}", j);
    }
}
