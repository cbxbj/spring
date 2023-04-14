package org.example.a_bean.e_beanFactoryPostProcessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Bean1 {
    public Bean1() {
        log.info("bean1 被 spring 管理了");
    }
}
