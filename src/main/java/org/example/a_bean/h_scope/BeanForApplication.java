package org.example.a_bean.h_scope;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Scope("application")
public class BeanForApplication {
    @PreDestroy
    public void destroy() {
        log.info("销毁:application");
    }
}
