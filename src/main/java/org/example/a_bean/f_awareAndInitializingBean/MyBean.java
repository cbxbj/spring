package org.example.a_bean.f_awareAndInitializingBean;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 先执行{@link Aware}
 * 再执行{@link InitializingBean}
 */
@Slf4j
public class MyBean implements BeanNameAware, ApplicationContextAware, InitializingBean {

    /**
     * 依赖注入阶段执行
     */
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public void aaa(ApplicationContext applicationContext) {
        log.info("使用@Autowired注入,当前的bean:{},容器:{}", this, applicationContext);
    }

    /**
     * 依赖注入阶段执行
     */
    @Override
    public void setBeanName(String name) {
        log.info("当前的bean:{},名字:{}", this, name);
    }

    /**
     * 依赖注入阶段执行
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("当前的bean:{},容器:{}", this, applicationContext);
    }

    /**
     * 初始化前执行
     */
    @PostConstruct
    public void init() {
        log.info("使用@PostConstruct,当前的bean:{},初始化", this);
    }

    /**
     * 初始化前执行
     */
    @Override
    public void afterPropertiesSet() {
        log.info("当前的bean:{},初始化", this);
    }
}
