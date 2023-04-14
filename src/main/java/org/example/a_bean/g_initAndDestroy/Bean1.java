package org.example.a_bean.g_initAndDestroy;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;

/**
 * 顺序:
 * {@link Aware}
 * {@link @PostConstruct}
 * {@link InitializingBean}
 * {@link Bean#initMethod()}
 */
@Slf4j
public class Bean1 implements BeanNameAware, InitializingBean {

    @Override
    public void setBeanName(String name) {
        log.info("依赖注入:aware");
    }

    @PostConstruct
    public void init() {
        log.info("初始化之前:@PostConstruct");
    }

    @Override
    public void afterPropertiesSet() {
        log.info("初始化之前:InitializingBean");
    }

    public void initMethod() {
        log.info("初始化之后:initMethod");
    }

}
