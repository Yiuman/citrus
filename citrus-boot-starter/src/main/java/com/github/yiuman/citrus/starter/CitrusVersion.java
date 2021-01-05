package com.github.yiuman.citrus.starter;

/**
 * Citrus的版本号
 *
 * @author yiuman
 * @date 2021/1/5
 */
public class CitrusVersion {

    public static final String Banner =
            " ____  _  _____  ____  _     ____ \n" +
                    "/   _\\/ \\/__ __\\/  __\\/ \\ /\\/ ___\\\n" +
                    "|  /  | |  / \\  |  \\/|| | |||    \\\n" +
                    "|  \\__| |  | |  |    /| \\_/|\\___ |\n" +
                    "\\____/\\_/  \\_/  \\_/\\_\\\\____/\\____/\n" +
                    "                                  ";
    private static final String CITRUS = " :: Citrus :: ";

    public CitrusVersion() {
    }

    public static void printBanner() {
        System.out.println(Banner);
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
