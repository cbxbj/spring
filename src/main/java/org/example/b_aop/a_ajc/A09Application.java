package org.example.b_aop.a_ajc;

import lombok.extern.slf4j.Slf4j;
import org.example.b_aop.a_ajc.service.MyService;

/**
 * 编译阶段修改字节码
 * 使用 aspectj 增强
 * 直接修改字节码，可增强静态方法
 * 添加方式:在maven中添加 aspectj-maven-plugin 插件
 */
@Slf4j
public class A09Application {
    public static void main(String[] args) {
        MyService.foo();
    }
}
