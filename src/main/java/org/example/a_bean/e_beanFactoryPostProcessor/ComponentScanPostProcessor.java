package org.example.a_bean.e_beanFactoryPostProcessor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

/**
 * 解析
 * {@link ComponentScan}
 * 1、寻找{@link ComponentScan#basePackages()}
 * 2、循环上一步中的结果找 加/间接加{@link Component}的类
 * 3、将上一步的结果加载到ioc中
 */
@Slf4j
public class ComponentScanPostProcessor implements BeanDefinitionRegistryPostProcessor {

    /**
     * context.refresh() 调用时会回调执行
     */
    @Override
    @SneakyThrows
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanFactory) throws BeansException {
        // 寻找 指定类上的 指定注解
        ComponentScan componentScan = AnnotationUtils.findAnnotation(BeanFactoryPostProcessorConfig.class, ComponentScan.class);
        if (componentScan == null) {
            return;
        }
        CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
        BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
        for (String basePackage : componentScan.basePackages()) {
            log.info("basePackage:{}", basePackage);
            // 转换        org.example.a_bean.e_beanFactoryPostProcessor.component -->
            // classpath*:org/example/a_bean/e_beanFactoryPostProcessor/component/**/*.class
            basePackage = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class";
            log.info("basePackage:{}", basePackage);
            // 根据通配符获取资源
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(basePackage);
            for (Resource resource : resources) {
                log.info("resource:{}", resource);
                // 不走类加载,效率比反射高
                MetadataReader reader = factory.getMetadataReader(resource);
                String className = reader.getClassMetadata().getClassName();
                log.info("className:{}", className);
                // 查看类上是直接加 指定注解或者
                boolean hasAnnotation = reader.getAnnotationMetadata().hasAnnotation(Component.class.getName());
                // 查看类上是间接加 指定注解或者
                boolean hasMetaAnnotation = reader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName());
                log.info("has @Component:{}", hasAnnotation);
                log.info("has @Component 派生:{}", hasMetaAnnotation);
                if (hasAnnotation || hasMetaAnnotation) {
                    AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder
                            .genericBeanDefinition(className)
                            .getBeanDefinition();
                    // 获取 bean 的名称
                    String beanName = beanNameGenerator.generateBeanName(beanDefinition, beanFactory);
                    log.info("beanName:{}", beanName);
                    //将 bean 注册到 ioc 中
                    beanFactory.registerBeanDefinition(beanName, beanDefinition);
                }
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}

