package org.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.example.error.ExceptionLogger;

public class LoggingProxy implements InvocationHandler {

    private final Object target;

    public LoggingProxy(final Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(target, args);
        } catch (Exception e) {
            ExceptionLogger.logException(e);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final T target, final Class<T> interfaceType) {
        return (T) java.lang.reflect.Proxy.newProxyInstance(interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                new LoggingProxy(target));
    }

}
