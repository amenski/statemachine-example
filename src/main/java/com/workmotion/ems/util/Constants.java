package com.workmotion.ems.util;

public class Constants {

    // LOGGER
    public static final String PARAMETER_2 = "{} {}";
    public static final String PARAMETER_3 = "{} {} {}";
    public static final String METHOD_START = " method start.";
    public static final String METHOD_END = " method end.";
    public static final String INPUT_PARAMETER = " input parameter ";

    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    
    public static final String UUID_KEY = "UUID";
    public static final String TRANSACTION_ID_KEY = "TRANSACTION_ID_KEY";

    private Constants() {
        throw new IllegalStateException("Utility class.");
    }
}
