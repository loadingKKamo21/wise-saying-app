package org.example.error;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExceptionLogger {

    private static final String LOG_DIR = "./log";
    private static final String LOG_FILE = "app.log";

    public static void logException(final Exception e) {
        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String logMessage = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_hh:mm:ss")) +
                " - Exception: " + e.getMessage();
        System.err.println(logMessage);
        e.printStackTrace();

        String filePath = LOG_DIR + File.separator + LOG_FILE;
        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, true))) {
            pw.println(logMessage);
            e.printStackTrace(pw);
        } catch (IOException ex) {
            System.err.println("Failed to write log to file: " + ex.getMessage());
        }
    }

}
