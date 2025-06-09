package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingService {

    private static final Logger logger = LoggerFactory.getLogger("CalcLogger");

    private LoggingService() {
    }

    public static Logger getLogger() {
        return logger;
    }
}
