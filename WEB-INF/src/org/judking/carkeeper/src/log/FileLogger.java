package org.judking.carkeeper.src.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by leilf on 2016/11/12.
 */
public class FileLogger {
    private static final FileLogger logger = new FileLogger();
    private static final String FILENAME = "D:\\CarKeeper\\CarKeeperServer\\WEB-INF\\logs\\CarKeeperLog.txt";
    private OutputStream out = null;

    private FileLogger() {
        try {
            out = new FileOutputStream(new File(FILENAME));
        } catch (Exception ex) {

        }
    }

    public static void i(String tag, String content) {
        try {
            if (logger.out != null) {
                logger.out.write((tag + ": " + content + "\r\n").getBytes());
            }
        } catch (Exception ex) {

        }
    }

    public static void access(String method, String url) {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        try {
            if (logger.out != null) {
                logger.out.write((date + ": " + method + " " + url + "\r\n").getBytes());
            }
        } catch (Exception ex) {

        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            if (out != null) {
                out.close();
                out = null;
            }
        } catch (Exception ex) {

        }
    }
}
