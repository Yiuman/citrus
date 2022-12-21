package com.github.yiuman.citrus.support.utils;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.bytecode.SignatureAttribute;

import java.util.StringTokenizer;

/**
 * @author yiuman
 * @date 2020/7/20
 */
public final class JavassistUtils {

    private static final ClassPool POOL = ClassPool.getDefault();

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

    /**
     * 添加类的泛型
     *
     * @param ctClass                  Javassist的类型
     * @param supperClass              父类
     * @param superTypeArgsClasses     父类的泛型类型
     * @param interfaceClass           接口
     * @param interfaceTypeArgsClasses 接口的泛型类型
     */
    public static void addTypeArgument(CtClass ctClass, Class<?> supperClass,
                                       Class<?>[] superTypeArgsClasses,
                                       Class<?> interfaceClass,
                                       Class<?>[] interfaceTypeArgsClasses) {

        SignatureAttribute.ClassType supperClassType = getSignatureClassType(supperClass, superTypeArgsClasses);
        //完整的接口泛型描述
        SignatureAttribute.ClassType interFaceClassType = getSignatureClassType(interfaceClass, interfaceTypeArgsClasses);
        ctClass.setGenericSignature(new SignatureAttribute
                .ClassSignature(null, supperClassType, interFaceClassType == null ? null : new SignatureAttribute.ClassType[]{interFaceClassType})
                .encode());
    }

    /**
     * 获取泛型描述类型
     *
     * @param mainClass       主类型
     * @param typeArgsClasses 泛型 按顺序
     * @return 泛型描述
     */
    public static SignatureAttribute.ClassType getSignatureClassType(Class<?> mainClass, Class<?>... typeArgsClasses) {
        if (mainClass == null) {
            return null;
        }
        return new SignatureAttribute.ClassType(mainClass.getName(), getSignatureTypeArguments(typeArgsClasses));
    }

    /**
     * 获取泛型描述数组
     *
     * @param classes 泛型的类型
     * @return 泛型参数数组
     */
    public static SignatureAttribute.TypeArgument[] getSignatureTypeArguments(Class<?>... classes) {
        if (classes == null || classes.length == 0) {
            return null;
        }
        SignatureAttribute.TypeArgument[] typeArguments = new SignatureAttribute.TypeArgument[classes.length];
        for (int i = 0; i < classes.length; i++) {
            typeArguments[i] = new SignatureAttribute.TypeArgument(new SignatureAttribute.ClassType(classes[i].getName()));
        }
        return typeArguments;
    }
}
