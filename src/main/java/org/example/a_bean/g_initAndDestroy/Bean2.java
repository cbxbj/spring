package org.example.a_bean.g_initAndDestroy;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;

/**
 * 顺序:
 * {@link PreDestroy}
 * {@link DisposableBean}
 * {@link Bean#destroyMethod()}
 */
@Slf4j
public class Bean2 implements DisposableBean {

    @PreDestroy
    public void preDestroy() {
        log.info("销毁:@PreDestroy");
    }

    @Override
    public void destroy() {
        log.info("销毁:DisposableBean");
    }

    public void destroyMethod() {
        log.info("销毁:destroyMethod");
    }

}
