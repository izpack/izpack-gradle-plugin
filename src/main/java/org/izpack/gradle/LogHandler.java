package org.izpack.gradle;

import org.gradle.api.logging.Logger;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Forwards IzPack compiler log records to a Gradle logger.
 */
class LogHandler extends Handler {
    /** The Gradle logger that receives supported log records. */
    private final Logger logger;

    /**
     * Creates a handler that forwards records to the specified logger.
     *
     * @param logger the Gradle logger that receives log records
     */
    LogHandler(Logger logger) {
        this.logger = logger;
    }

    /** Does nothing for levels that are not forwarded to Gradle. */
    private void nop() {
        // no action
    }

    /**
     * Forwards supported Java logging levels to their corresponding Gradle levels.
     * {@code SEVERE}, {@code WARNING}, {@code CONFIG}/{@code INFO},
     * {@code FINE}/{@code FINER}, and {@code FINEST}/{@code ALL} map to
     * error, warn, info, debug, and trace respectively. {@code OFF} and
     * unrecognized levels are ignored.
     *
     * @param logRecord the record to publish
     */
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

    /** Does nothing; there is no buffered output to flush. */
    @Override
    public void flush() {
        // no action
    }

    /** Does nothing; closing this handler does not close the Gradle logger. */
    @Override
    public void close() throws SecurityException {
        // no action
    }
}
