package org.judking.carkeeper.src.util;

/**
 * Created by leilf on 2017/4/8.
 */
public class CommandEnum {
    // Engine
    public static final String PID_RPM = "010c"; // Engine RPM (rpm)
    public static final String PID_ENGINE_LOAD = "0104"; // Calculated engine load (%)
    public static final String PID_COOLANT_TEMP = "0105"; // Engine coolant temperature (°C)
    public static final String PID_ABSOLUTE_ENGINE_LOAD = "0143"; // Absolute Engine load (%)
    public static final String PID_TIMING_ADVANCE = "010e"; // Ignition timing advance (°)
    public static final String PID_ENGINE_OIL_TEMP = "015c"; // Engine oil temperature (°C)
    public static final String PID_ENGINE_TORQUE_PERCENTAGE = "0162"; //Engine torque percentage (%)
    public static final String PID_ENGINE_REF_TORQUE = "0163";  // Engine reference torque (Nm)

    // Intake / Exhaust
    public static final String PID_INTAKE_TEMP = "010f"; // Intake temperature (°C)
    public static final String PID_INTAKE_MAP = "010b"; //  Intake manifold absolute pressure (kPa)
    public static final String PID_MAF_FLOW = "0110"; // MAF flow pressure (grams/s)
    public static final String PID_BAROMETRIC = "0133";     // Barometric pressure (kPa)
    public static final String PID_SHORT_TERM_FUEL_TRIM_BANK_1 = "0106";
    public static final String PID_SHORT_TERM_FUEL_TRIM_BANK_2 = "0108";
    public static final String PID_LONG_TERM_FUEL_TRIM_BANK_1 = "0107";
    public static final String PID_LONG_TERM_FUEL_TRIM_BANK_2 = "0109"; // (%)
    public static final String PID_FUEL_PRESSURE = "010a";

    // Speed/Time
    public static final String PID_SPEED = "010d";  //  Vehicle speed (km/h)
    public static final String PID_RUNTIME = "011f";    //  Engine running time (second)
    public static final String PID_DISTANCE = "0131";   // Vehicle running distance (km)

    // Driver
    public static final String PID_THROTTLE = "0111";   // Throttle position (%)
    public static final String PID_AMBIENT_TEMP = "0146";   // Ambient temperature (°C)

    // Electric Systems
    public static final String PID_CONTROL_MODULE_VOLTAGE = "0142"; //vehicle control module voltage (V)
    public static final String PID_HYBRID_BATTERY_PERCENTAGE = "015b"; // Hybrid battery pack remaining life (%)

    // custom pid
    public static final String ACCELERATE = "1001";     // 加速度 (m/s^2)
    public static final String FUEL_RATE = "1002";      // 耗油速率
    public static final String LATITUDE = "1003";   // 纬度
    public static final String LONGITUDE = "1004";  // 经度
    public static final String CAR_PITCH = "1005";  // 车辆倾角

    public static final String PID_OXYGEN = "0115";
    public static final String PID_ACCELERATOR_PEDAL = "0149";
    public static final String PID_CATALYST_TEMP = "013c";

    public static final String[] OXYGEN_SENSOR = {PID_OXYGEN, PID_THROTTLE};
    public static final String[] CATALYST_SENSOR = {PID_OXYGEN, PID_CATALYST_TEMP};
    public static final String[] INJECTOR = {PID_FUEL_PRESSURE, PID_MAF_FLOW};
}
