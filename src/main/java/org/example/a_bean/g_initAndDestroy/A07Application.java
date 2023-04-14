package org.example.a_bean.g_initAndDestroy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 初始化和销毁
 */
@SpringBootApplication
public class A07Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A07Application.class, args);
        context.close();
    }

    @Bean(initMethod = "initMethod")
    public Bean1 bean1() {
        return new Bean1();
    }

    @Bean(destroyMethod = "destroyMethod")
    public Bean2 bean2() {
        return new Bean2();
    }
}
