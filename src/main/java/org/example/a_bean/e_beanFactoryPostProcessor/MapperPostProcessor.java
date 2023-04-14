package org.example.a_bean.e_beanFactoryPostProcessor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;

/**
 * 解析{@link Mapper}
 * 1、获取加了{@link Mapper} 的类
 * 2、排除不是接口的类
 * 3、生成对应的{@link MapperFactoryBean}
 * 4、将上一步生成的Bean定义 加到 ioc 中
 */
@Slf4j
public class MapperPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    @SneakyThrows
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanFactory) throws BeansException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String basePackage = "org.example.a_bean.e_beanFactoryPostProcessor.mapper";
        Resource[] resources = resolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class");
        CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
        BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
        for (Resource resource : resources) {
            // 不走类加载,效率比反射高
            MetadataReader metadataReader = factory.getMetadataReader(resource);
            ClassMetadata classMetadata = metadataReader.getClassMetadata();
            // 判断是否为接口
            if (!classMetadata.isInterface()) {
                return;
            }
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(MapperFactoryBean.class)
                    // 生成的MapperFactoryBean的参数
                    .addConstructorArgValue(classMetadata.getClassName())
                    // 方法的参数 装配类型
                    .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                    .getBeanDefinition();
            // 专门获取名字的 AbstractBeanDefinition
            AbstractBeanDefinition beanDefinitionName = BeanDefinitionBuilder.genericBeanDefinition(classMetadata.getClassName()).getBeanDefinition();
            String beanName = beanNameGenerator.generateBeanName(beanDefinitionName, beanFactory);
            beanFactory.registerBeanDefinition(beanName, beanDefinition);
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
