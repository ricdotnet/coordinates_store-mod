package dev.ricr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
    public static final String MOD_ID = "coordinates-store-mod";

    public static Logger LOGGER() {
        return LoggerFactory.getLogger(MOD_ID);
    }

}
