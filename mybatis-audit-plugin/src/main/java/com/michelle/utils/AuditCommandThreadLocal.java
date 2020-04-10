package com.michelle.utils;


import com.michelle.command.AuditCommand;
import com.michelle.command.BatchCommand;

/**
 * @author michelle.min
 */
public class AuditCommandThreadLocal {
    private static final ThreadLocal<BatchCommand> THREAD_LOCAL = new ThreadLocal<>();

    private AuditCommandThreadLocal() {
    }

    public static void set(BatchCommand batchCommand) {
        THREAD_LOCAL.set(batchCommand);
    }

    public static BatchCommand get() {
        return THREAD_LOCAL.get();
    }

    public static boolean add(AuditCommand command) {
        BatchCommand batchCommand = THREAD_LOCAL.get();
        if (batchCommand != null) {
            return batchCommand.add(command);
        }
        return false;
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
