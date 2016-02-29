package io.liamju.comm.util;

import android.util.Log;

/**
 * @author LiamJu
 * @version 1.0
 * @since 16/1/14
 */
@SuppressWarnings("unused")
public class L {

    private static boolean sIsLogEnable;

    /**
     * 启动 Log 日志
     */
    public static void enableLog() {
        sIsLogEnable = true;
    }

    /**
     * 禁用 Log 日志
     */
    public static void disableLog() {
        sIsLogEnable = false;
    }


    public static void d(String format, Object... args) {
        d(String.format(format, args));
    }

    public static void d(String msg) {
        if (sIsLogEnable) {
            Log.d(getClassName(), msg);
        }
    }

    public static void e(String msg) {
        if (sIsLogEnable) {
            Log.e(getClassName(), msg);
        }
    }

    public static void v(String msg) {
        if (sIsLogEnable) {
            Log.v(getClassName(), msg);
        }
    }

    public static void i(String msg) {
        if (sIsLogEnable) {
            Log.i(getClassName(), msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sIsLogEnable) {
            Log.i(tag, msg);
        }
    }

    private static String getClassName() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        String className = elements[4].getClassName();
        int lastIndex = className.lastIndexOf(".");
        String substring = className.substring(lastIndex + 1);
        //如果调用位置在匿名内部类的话，就会产生类似于 MainActivity$3 这样的TAG
        //可以把$后面的部分去掉
        int i = substring.indexOf("$");
        return i == -1 ? substring : substring.substring(0, i);
    }
}
