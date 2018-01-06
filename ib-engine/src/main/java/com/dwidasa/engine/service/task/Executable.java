package com.dwidasa.engine.service.task;

import java.util.Date;

/**
 * Common interface to all periodic task.
 *
 * @author rk
 */
public interface Executable {

    /**
     * Execute single task that belong to a period (bod, eod, ..)
     * @param processingDate processing date
     * @param userId user id
     * @throws Exception if failed
     */
    public void execute(Date processingDate, Long userId) throws Exception;

    /**
     * Perform cleanup when an exception occur during execute phase.
     * @param processingDate processing date
     */
    public void cleanup(Date processingDate);
}
