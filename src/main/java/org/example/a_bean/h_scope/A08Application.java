package org.example.a_bean.h_scope;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.a_bean.h_scope.lapse.E;
import org.example.a_bean.h_scope.lapse.F1;
import org.example.a_bean.h_scope.lapse.F2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Scope
 * singleton:单例
 * prototype:多例
 * request、session、application
 * 反射: --add-opens java.base/java.lang=ALL-UNNAMED
 */
@Slf4j
@SpringBootApplication
public class A08Application {

    @Autowired
    private E e;

    public static void main(String[] args) {
        SpringApplication.run(A08Application.class, args);

    }

    @PostConstruct
    public void init() {
        F1 f1 = e.getF1();
        log.info("f1:{}", f1);
        log.info("f1 class:{}", f1.getClass());
        f1 = e.getF1();
        log.info("f1:{}", f1);
        log.info("f1 class:{}", f1.getClass());
        F2 f2 = e.getF2();
        log.info("f2:{}", f2);
        log.info("f2 class:{}", f2.getClass());
        f2 = e.getF2();
        log.info("f2:{}", f2);
        log.info("f2 class:{}", f2.getClass());
        log.info("f3:{}", e.getF3());
        log.info("f3:{}", e.getF3());
        log.info("f4:{}", e.getF4());
        log.info("f4:{}", e.getF4());
    }
}
