package org.example.a_bean.c_lifeCycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Slf4j
public class LifeCycleBean implements ApplicationContextAware, InitializingBean, DisposableBean {

    public LifeCycleBean() {
        log.info("1.构造方法");
    }

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public void autowire(@Value("${java.home}") String home) {
        log.info("2.依赖注入:{}", home);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("3.[Aware] 依赖注入:设置applicationContext");
    }

    @PostConstruct
    public void init() {
        log.info("4.[@PostConstruct]初始化之前执行");
    }

    @Override
    public void afterPropertiesSet() {
        log.info("5.[InitializingBean]初始化之前执行");
    }

    public void initMethod() {
        log.info("6.[@Bean(initMethod)]初始化之后执行");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("1.销毁[@PreDestroy]");
    }

    @Override
    public void destroy() {
        log.info("2.销毁[DisposableBean]");
    }

    public void destroyMethod() {
        log.info("3.销毁[@Bean]destroyMethod");
    }
}
