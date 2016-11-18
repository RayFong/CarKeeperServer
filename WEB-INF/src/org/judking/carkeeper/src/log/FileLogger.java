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
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]: ");
    private OutputStream out = null;

    private FileLogger() {
        try {
            String path = System.getProperty("user.dir") + File.separator + ".." + File.separator + "logs"
                    + File.separator + "CarKeeperServerLog.txt";
            out = new FileOutputStream(new File(path));
        } catch (Exception ignored) {

        }
    }

    public static void i(String tag, String content) {
        try {
            if (logger.out != null) {
                logger.out.write((tag + ": " + content + "\r\n").getBytes());
            }
        } catch (Exception ignored) {

        }
    }

    public static void access(String method, String url) {
        String date = dateFormat.format(new Date());
        try {
            if (logger.out != null) {
                logger.out.write((date + method + " " + url + "\r\n").getBytes());
            }
        } catch (Exception ignored) {

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
