package com.michelle.utils;


import com.michelle.command.AuditCommand;

import java.util.List;

/**
 * @author michelle.min
 */
public class AuditCommandThreadLocal {
    private static final ThreadLocal<List<AuditCommand>> THREAD_LOCAL = new ThreadLocal<>();

    private AuditCommandThreadLocal() {
    }

    public static void set(List<AuditCommand> executorList) {
        THREAD_LOCAL.set(executorList);
    }

    public static List<AuditCommand> get() {
        return THREAD_LOCAL.get();
    }

    public static boolean add(AuditCommand command) {
        List<AuditCommand> executorList = THREAD_LOCAL.get();
        if (executorList != null) {
            return executorList.add(command);
        }
        return false;
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
