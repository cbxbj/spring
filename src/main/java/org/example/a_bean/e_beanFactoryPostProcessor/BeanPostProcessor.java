package org.example.a_bean.e_beanFactoryPostProcessor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.util.Objects;
import java.util.Set;

/**
 * 解析 {@link Bean}
 * 1、寻找添加{@link Configuration} 的类
 * 2、循环找到上一步中的类 包含 {@link Bean}的方法
 * 3、将该方法加到 ioc 中
 */
@Slf4j
public class BeanPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    @SneakyThrows
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanFactory) throws BeansException {
        CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
        // 不走类加载,效率比反射高
        MetadataReader metadataReader = factory.getMetadataReader(new ClassPathResource("org/example/a_bean/e_beanFactoryPostProcessor/BeanFactoryPostProcessorConfig.class"));
        // 找到该类中被 @Bean 标注的方法
        Set<MethodMetadata> methods = metadataReader.getAnnotationMetadata().getAnnotatedMethods(Bean.class.getName());
        for (MethodMetadata method : methods) {
            log.info("method:{}", method);
            // 获取该注解的属性
            String initMethod = Objects.requireNonNull(method.getAnnotationAttributes(Bean.class.getName())).get("initMethod").toString();
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
            // 因为是通过工厂类加载的Bean 所以需要工厂类的信息
            builder.setFactoryMethodOnBean(method.getMethodName(), "config");
            // 自动装配 默认 no,对于构造方法/工厂方法参数 自动装配需选择该方式
            builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
            if (initMethod.length() > 0) {
                // 初始化方法
                builder.setInitMethodName(initMethod);
            }
            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
            beanFactory.registerBeanDefinition(method.getMethodName(), beanDefinition);

        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
