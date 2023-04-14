package org.example.a_bean.f_awareAndInitializingBean;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MyConfig {
    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        log.info("注入ApplicationContext:{}", applicationContext);
    }

    @PostConstruct
    public void init() {
        log.info("初始化");
    }

    /**
     * 配置类中加beanFactory后处理器则{@link Autowired}、{@link  PostConstruct}会失效
     */
    @Bean
    public /*static*/ BeanFactoryPostProcessor processor1() {
        return beanFactory -> log.info("执行processor1");
    }
}
