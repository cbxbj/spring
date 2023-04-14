package org.example.b_aop.f_methodProxy;

import lombok.extern.slf4j.Slf4j;
import org.example.b_aop.e_cglibProxy.Proxy;
import org.springframework.cglib.core.Signature;

@Slf4j
public class ProxyFastClass {
    static Signature S0 = new Signature("saveSuper", "()V");
    static Signature S1 = new Signature("saveSuper", "(I)V");
    static Signature S2 = new Signature("saveSuper", "(J)V");

    /**
     * 获取目标对象方法的编号
     * saveSuper()           0
     * saveSuper(int)        1
     * saveSuper(long)       2
     */
    public int getIndex(Signature signature) {
        if (S0.equals(signature)) {
            return 0;
        } else if (S1.equals(signature)) {
            return 1;
        } else if (S2.equals(signature)) {
            return 2;
        } else {
            return -1;
        }
    }

    /**
     * 根据编号 正常调用 目标对象的方法
     */
    public Object invoke(int index, Object proxy, Object[] args) {
        if (index == 0) {
            ((Proxy) proxy).saveSuper();
            return null;
        } else if (index == 1) {
            ((Proxy) proxy).saveSuper((int) args[0]);
            return null;
        } else if (index == 2) {
            ((Proxy) proxy).saveSuper((long) args[0]);
            return null;
        } else {
            throw new RuntimeException("无此方法");
        }
    }

    public static void main(String[] args) {
        ProxyFastClass proxyFastClass = new ProxyFastClass();
        int index = proxyFastClass.getIndex(new Signature("saveSuper", "()V"));
        log.info("index:{}", index);
        proxyFastClass.invoke(index, new Proxy(), new Object[0]);
    }
}
