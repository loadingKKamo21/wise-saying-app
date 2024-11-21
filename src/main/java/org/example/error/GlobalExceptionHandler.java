package org.example.error;

public class GlobalExceptionHandler {

    public static void initialize() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.err.println("Uncaught exception in thread " + t.getName());
            ExceptionLogger.logException((Exception) e);
        });
    }

}
