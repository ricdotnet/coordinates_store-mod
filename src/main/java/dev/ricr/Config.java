package dev.ricr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
    public static Logger LOGGER() {
        return LoggerFactory.getLogger(Constants.MOD_ID);
    }
}
