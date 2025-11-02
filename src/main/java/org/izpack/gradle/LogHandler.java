package org.izpack.gradle;

import org.gradle.api.logging.Logger;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

class LogHandler extends Handler {
    private final Logger logger;

    LogHandler(Logger logger) {
        this.logger = logger;
    }

    private void nop() {
        // no action
    }

    @Override
    public void publish(LogRecord logRecord) {
        switch (logRecord.getLevel().getName()) {
            case "SEVERE" -> logger.error(logRecord.getMessage(), logRecord.getThrown());
            case "WARNING" -> logger.warn(logRecord.getMessage(), logRecord.getThrown());
            case "CONFIG", "INFO" -> logger.info(logRecord.getMessage(), logRecord.getThrown());
            case "FINE", "FINER" -> logger.debug(logRecord.getMessage(), logRecord.getThrown());
            case "FINEST", "ALL" -> logger.trace(logRecord.getMessage(), logRecord.getThrown());
            case "OFF" -> nop();
            default -> nop();
        }
    }

    @Override
    public void flush() {
        // no action
    }

    @Override
    public void close() throws SecurityException {
        // no action
    }
}
