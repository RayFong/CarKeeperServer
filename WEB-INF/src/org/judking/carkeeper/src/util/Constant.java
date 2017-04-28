package org.judking.carkeeper.src.util;

import java.io.File;

/**
 * Created by leilf on 2017/4/27.
 */
public class Constant {
    public static final String BASE_MODEL_FILE_PATH = System.getProperty("user.dir") + File.separator + ".." + File.separator + ".." +
            File.separator + "model" + File.separator;

    public static final String OXYGEN_SENSOR_MODEL_NAME = "oxygen.model";

    public static final String CATALYST_SENSOR_MODEL_NAME = "catalyst.model";

    public static final String INJECTOR_MODEL_NAME = "injector.model";
}
