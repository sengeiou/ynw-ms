package com.ynw.system.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtil {
    private static ExecutorService executorService= Executors.newCachedThreadPool();
    public static void execute(Runnable runnable){
        executorService.execute(runnable);
    }

}
