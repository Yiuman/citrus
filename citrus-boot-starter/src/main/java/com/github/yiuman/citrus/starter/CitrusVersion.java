package com.github.yiuman.citrus.starter;

import cn.hutool.core.io.IoUtil;

/**
 * Citrus的版本号
 *
 * @author yiuman
 * @date 2021/1/5
 */
public class CitrusVersion {

    public static final String BANNER = IoUtil.readUtf8(CitrusVersion.class.getClassLoader().getResourceAsStream("META-INF/banner.txt"));
    private static final String CITRUS = " :: Citrus :: ";

    public static void printBanner() {
        System.out.println(BANNER);
        System.out.printf(CITRUS + "v%s%n", getVersion());
    }

    public static String getVersion() {
        final Package pkg = CitrusVersion.class.getPackage();
        if (pkg != null && pkg.getImplementationVersion() != null) {
            return pkg.getImplementationVersion();
        }
        return null;
    }

}
