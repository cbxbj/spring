package org.example.a_bean.h_scope.lapse;

import lombok.Getter;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 * 解决单例类中注入非单例类失效问题
 * 1、加{@link Lazy} 注入时是注入代理类,每次调用方法时是由代理类创建
 * 2、在被注入的类中 {@link Scope#proxyMode()} 设置为{@link ScopedProxyMode#TARGET_CLASS}
 * 3、通过{@link ObjectFactory} 获取
 * 4、通过{@link ApplicationContext} 获取
 * 其中:1 2 是通过代理, 3 4 的效率更高
 */
@Getter
@Component
public class E {

    @Lazy
    @Autowired
    private F1 f1;

    @Autowired
    private F2 f2;

    @Autowired
    private ObjectFactory<F3> f3;

    public F3 getF3() {
        return f3.getObject();
    }

    @Autowired
    private ApplicationContext context;

    public F4 getF4() {
        return context.getBean(F4.class);
    }
}
