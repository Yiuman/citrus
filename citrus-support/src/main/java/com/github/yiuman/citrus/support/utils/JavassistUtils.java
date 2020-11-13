package com.github.yiuman.citrus.support.utils;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;

import java.util.StringTokenizer;

/**
 * @author yiuman
 * @date 2020/7/20
 */
public class JavassistUtils {

    private static final ClassPool POOL = ClassPool.getDefault();

    private JavassistUtils() {
    }

    public static ClassPool defaultPool() {
        return POOL;
    }

    public static CtClass getClass(Class<?> c) throws Exception {
        return POOL.get(c.getName());
    }

    public static CtClass getClass(String className) throws Exception {
        return POOL.get(className);
    }

    public static CtClass makeClass(String className) throws Exception {
        return POOL.makeClass(className);
    }

    public static void addClassPath(String path) throws Exception {
        POOL.appendClassPath(path);
    }

    static {
        //添加当前线程的ClassPath
        POOL.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        StringTokenizer token = new StringTokenizer(System.getProperty("java.class.path"), System.getProperty("path.separator"));

        while (token.hasMoreElements()) {
            try {
                String classPath = token.nextToken();
                POOL.appendClassPath(classPath);
            } catch (Throwable ignore) {
            }
        }


    }
}
