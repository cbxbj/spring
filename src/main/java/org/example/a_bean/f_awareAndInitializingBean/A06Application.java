package org.example.a_bean.f_awareAndInitializingBean;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

/**
 * {@link Aware} 、{@link InitializingBean}
 * <p>
 * {@link Aware}: 注入一些与容器相关的信息
 * {@link BeanNameAware} 注入 Bean 的名字
 * {@link BeanFactoryAware} 注入 BeanFactory 容器
 * {@link ApplicationContextAware} 注入 ApplicationContext 容器
 * {@link EmbeddedValueResolverAware} 注入解析器,解析${}
 * <p>
 * {@link InitializingBean}:给Bean添加一些初始化方法
 */
@Slf4j
public class A06Application {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();

        //testAwareAndInitializingBean(context);

        // 测试失效
        testLapse(context);

        context.refresh();
        context.close();
    }

    /**
     * 在配置类中加beanFactory后处理器后,该配置类中的所有与扩展有关的功能都会失效
     * 原因:
     * context.refresh()顺序
     * 1、执行 beanFactory 后处理器
     * 2、注册 bean 后处理器
     * 3、执行Bean的 构造 --> {@link Autowired} {@link Value}等 依赖注入 --> 初始化 --> 销毁
     * <p>
     * 若在配置类中配置了 beanFactory 后处理器
     * 1、先创建配置类 创建之后,配置类已经走完 构造 --> 依赖注入 --> 初始化
     * 2、执行{@link Aware} 、{@link InitializingBean}
     * 3、执行 beanFactory 后处理器 注册 bean 后处理器
     */
    private static void testLapse(GenericApplicationContext context) {
        context.registerBean(MyConfig.class);
        addBeanPostProcessor(context);
    }

    /**
     * 为什么不使用{@link Autowired}、{@link PostConstruct} 替换 {@link Aware} 、{@link InitializingBean}
     * {@link Autowired}的解析需要用到 bean 后处理器 属于扩展功能
     * {@link Aware}属于内置功能，不加任何扩展
     */
    @SuppressWarnings("unused")
    private static void testAwareAndInitializingBean(GenericApplicationContext context) {
        context.registerBean("我的Bean", MyBean.class);
        // 后处理器
        //addBeanPostProcessor(context);
    }

    private static void addBeanPostProcessor(GenericApplicationContext context) {
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        context.registerBean(CommonAnnotationBeanPostProcessor.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
    }
}
