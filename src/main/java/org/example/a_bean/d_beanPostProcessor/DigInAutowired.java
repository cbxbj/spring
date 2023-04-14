package org.example.a_bean.d_beanPostProcessor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * {@link AutowiredAnnotationBeanPostProcessor} 运行分析
 * 1、寻找加了{@link Autowired}的变量或方法
 * 2、在 ioc 中获取并将值反射赋值给变量或方法参数
 */
@Slf4j
public class DigInAutowired {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 该方法 提供的是一个成品 Bean 不会在执行 构造、依赖注入、初始化
        beanFactory.registerSingleton("bean2", new Bean2());
        beanFactory.registerSingleton("bean3", new Bean3());

        // 解析 @Value 中的内容
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());

        // ${} 解析器
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);

        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(beanFactory);

        Bean1 bean1 = new Bean1();
        log.info("bean1:{}", bean1);

        //processor.postProcessProperties(null, bean1, "bean1"); // 依赖注入阶段会调用 解析 @Autowired @Value

        InjectionMetadata metadata = findAutowiringMetadata(processor);

        //metadata.inject(bean1, "bean1", null);

        inject(beanFactory);

        log.info("bean1:{}", bean1);

    }

    /**
     * 获取 Bean1 上 加了 {@link Autowired} {@link Value} 的成员变量、方法参数信息
     */
    @SneakyThrows
    private static InjectionMetadata findAutowiringMetadata(AutowiredAnnotationBeanPostProcessor processor) {
        Method findAutowiringMetadataMethod = AutowiredAnnotationBeanPostProcessor.class
                .getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        findAutowiringMetadataMethod.setAccessible(true);
        InjectionMetadata metadata = (InjectionMetadata) findAutowiringMetadataMethod.invoke(processor, "bean1", Bean1.class, null);

        log.info("metadata:{}", metadata);
        return metadata;
    }

    /**
     * 在 ioc 中寻找 并赋值
     */
    @SneakyThrows
    private static void inject(DefaultListableBeanFactory beanFactory) {

        Field bean3Field = Bean1.class.getDeclaredField("bean3");
        // 参数2: 是否必须,true: ioc中找不到则报错
        DependencyDescriptor dd1 = new DependencyDescriptor(bean3Field, false);
        // 根据类型找到 Bean3
        Bean3 bean3 = (Bean3) beanFactory.doResolveDependency(dd1, null, null, null);
        log.info("bean3:{}", bean3);

        Method setBean2Method = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);
        DependencyDescriptor dd2 = new DependencyDescriptor(new MethodParameter(setBean2Method, 0), false);
        Bean2 bean2 = (Bean2) beanFactory.doResolveDependency(dd2, null, null, null);
        log.info("bean2:{}", bean2);

        Method setHomeMethod = Bean1.class.getDeclaredMethod("setHome", String.class);
        DependencyDescriptor dd3 = new DependencyDescriptor(new MethodParameter(setHomeMethod, 0), false);
        String home = (String) beanFactory.doResolveDependency(dd3, null, null, null);
        log.info("home:{}", home);
    }
}
