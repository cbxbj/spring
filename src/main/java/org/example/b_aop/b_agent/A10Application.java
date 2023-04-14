package org.example.b_aop.b_agent;

import lombok.extern.slf4j.Slf4j;
import org.example.b_aop.b_agent.service.MyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 类加载阶段修改
 * 使用 agent 增强
 * -javaagent:xx/xx/aspectjweaver-1.9.7.jar
 */
@Slf4j
@SpringBootApplication
public class A10Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A10Application.class, args);
        MyService service = context.getBean(MyService.class);
        log.info("service class:{}", service.getClass());
        service.foo();
    }
}
