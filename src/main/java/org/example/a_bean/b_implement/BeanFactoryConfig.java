package org.example.a_bean.b_implement;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanFactoryConfig {
    @Bean
    public Bean1 bean1() {
        return new Bean1();
    }

    @Bean
    public Bean2 bean2() {
        return new Bean2();
    }

    @Bean
    public Bean3 bean3() {
        return new Bean3();
    }

    @Bean
    public Bean4 bean4() {
        return new Bean4();
    }
}


@Slf4j
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
class Bean1 {
    @Autowired
    private Bean2 bean2;

    /**
     * 默认:bean3
     * 若添加了比较器,则为:bean4
     */
    @Autowired
    @Resource(name = "bean4")
    private Inter bean3;

    public Bean1() {
        log.info("构造 Bean1");
    }

    public Bean2 getBean2() {
        return bean2;
    }

    public Inter getInter() {
        return bean3;
    }
}

@Slf4j
class Bean2 {
    public Bean2() {
        log.info("构造 Bean2");
    }
}

interface Inter {

}

class Bean3 implements Inter {

}

class Bean4 implements Inter {

}


