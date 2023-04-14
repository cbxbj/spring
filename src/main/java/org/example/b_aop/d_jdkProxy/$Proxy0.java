package org.example.b_aop.d_jdkProxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

@Slf4j
public class $Proxy0 extends Proxy implements Foo {

    //private final InvocationHandler h;

    public $Proxy0(InvocationHandler h) {
        //this.h = h;
        super(h);
    }

    static Method fooMethod;
    static Method barMethod;

    static {
        try {
            fooMethod = Foo.class.getMethod("foo");
            barMethod = Foo.class.getMethod("bar");
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }
    }

    @Override
    public void foo() {
        try {
            h.invoke(this, fooMethod, new Object[0]);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    @Override
    public int bar() {
        try {
            return (int) h.invoke(this, barMethod, new Object[0]);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}
