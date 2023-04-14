package org.example.b_aop.f_methodProxy;

import lombok.extern.slf4j.Slf4j;
import org.example.b_aop.e_cglibProxy.Target;
import org.springframework.cglib.core.Signature;

@Slf4j
public class TargetFastClass {

    static Signature S0 = new Signature("save", "()V");
    static Signature S1 = new Signature("save", "(I)V");
    static Signature S2 = new Signature("save", "(J)V");

    /**
     * 获取目标对象方法的编号
     * save()           0
     * save(int)        1
     * save(long)       2
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
    public Object invoke(int index, Object target, Object[] args) {
        if (index == 0) {
            ((Target) target).save();
            return null;
        } else if (index == 1) {
            ((Target) target).save((int) args[0]);
            return null;
        } else if (index == 2) {
            ((Target) target).save((long) args[0]);
            return null;
        } else {
            throw new RuntimeException("无此方法");
        }
    }

    public static void main(String[] args) {
        TargetFastClass fastClass = new TargetFastClass();
        int index = fastClass.getIndex(new Signature("save", "()V"));
        log.info("index:{}", index);
        fastClass.invoke(index, new Target(), new Object[0]);
    }
}
