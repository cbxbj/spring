package org.example.a_bean.e_beanFactoryPostProcessor;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;

@Slf4j
public class A05Application {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config", BeanFactoryPostProcessorConfig.class);

        parseConfigurationClass(context);

        parseMapperScanner(context);

        context.refresh();
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            log.info("beanDefinitionName:{}", beanDefinitionName);
        }
        context.close();
    }

    /**
     * 解析
     * {@link ComponentScan}
     * {@link Bean}
     * {@link Import}
     * {@link ImportResource}
     */
    private static void parseConfigurationClass(GenericApplicationContext context) {
        //context.registerBean(ConfigurationClassPostProcessor.class);
        context.registerBean(ComponentScanPostProcessor.class);
        context.registerBean(BeanPostProcessor.class);
    }

    /**
     * 解析
     * {@link MapperScan}
     */
    private static void parseMapperScanner(GenericApplicationContext context) {
        //context.registerBean(MapperScannerConfigurer.class, bd -> bd.getPropertyValues().add("basePackage", "org.example.a_bean.e_beanFactoryPostProcessor.mapper"));
        context.registerBean(MapperPostProcessor.class);
    }
}
