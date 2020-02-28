package com.michelle.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;


/**
 * @author michelle.min
 */
public class AuditThreadUtil {
    private static ExecutorService EXECUTORSERVICE;

    private AuditThreadUtil() {
    }

    public static void initial(int corePoolSize,
                               int maxPoolSize,
                               long keepAliveTime,
                               int queueCapacity) {
        if (EXECUTORSERVICE == null) {
            EXECUTORSERVICE = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(queueCapacity),
                    new BasicThreadFactory.Builder().namingPattern("auditobject-pool-%d").build());
        }
    }

    public static <T> Future<T> submit(Callable<T> task) {
        return EXECUTORSERVICE.submit(task);
    }


}
