package io.liamju.comm.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author LiamJu
 * @version 1.0
 * @since 16/2/19
 */
public class MyUtils {
    public static void close(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
