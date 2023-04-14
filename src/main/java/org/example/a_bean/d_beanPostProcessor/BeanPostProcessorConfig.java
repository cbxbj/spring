package org.example.a_bean.d_beanPostProcessor;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;


public class BeanPostProcessorConfig {
}

@Slf4j
@ToString
class Bean1 {
    private Bean2 bean2;

    private Bean3 bean3;

    private String home;

    @Autowired
    public void setBean2(Bean2 bean2) {
        log.info("@Autowired 生效:{}", bean2);
        this.bean2 = bean2;
    }

    @Resource
    public void setBean3(Bean3 bean3) {
        log.info("@Resource 生效:{}", bean3);
        this.bean3 = bean3;
    }

    @Autowired
    public void setHome(@Value("${java.home}") String home) {
        log.info("@Value 生效:{}", home);
        this.home = home;
    }

    @PostConstruct
    public void init() {
        log.info("@PostConstruct 生效");
    }

    @PreDestroy
    public void destroy() {
        log.info("@PreDestroy 生效");
    }
}

class Bean2 {

}

class Bean3 {

}

@Data
@ConfigurationProperties(prefix = "java")
class Bean4 {
    private String home;

    private String version;

}
