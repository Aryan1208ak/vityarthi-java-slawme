package com.vityarthi.slawme.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class for thread-safe system audit logging.
 */
public class AuditLogger {
    private static AuditLogger instance;
    private final String logFilePath = "data/audit.log";
    private final List<String> memoryLogs = new ArrayList<>();
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AuditLogger() {
        // Ensure data directory exists
        java.io.File dir = new java.io.File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static synchronized AuditLogger getInstance() {
        if (instance == null) {
            instance = new AuditLogger();
        }
        return instance;
    }

    public synchronized void log(String level, String actor, String action) {
        String timestamp = LocalDateTime.now().format(dtf);
        String logEntry = String.format("[%s] [%-5s] [%-10s] : %s", timestamp, level, actor, action);
        memoryLogs.add(logEntry);

        // Write to file asynchronously or synchronously
        try (PrintWriter out = new PrintWriter(new FileWriter(logFilePath, true))) {
            out.println(logEntry);
        } catch (IOException e) {
            System.err.println("Audit Logger Exception: " + e.getMessage());
        }
    }

    public synchronized List<String> getRecentLogs(int count) {
        int start = Math.max(0, memoryLogs.size() - count);
        return new ArrayList<>(memoryLogs.subList(start, memoryLogs.size()));
    }
}
