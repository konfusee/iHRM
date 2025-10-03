package com.ihrm.web.listener;

import com.ihrm.web.session.SessionManager;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Listener that manages periodic cleanup of expired sessions
 */
@WebListener
public class SessionCleanupListener implements ServletContextListener {
    private ScheduledExecutorService scheduler;

    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Initializing session cleanup service...");

        // Get cleanup interval from configuration
        int cleanupIntervalMinutes = Integer.parseInt(
            getEnvOrDefault("SESSION_CLEANUP_INTERVAL_MINUTES", "30"));

        // Create scheduled executor
        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "SessionCleanup");
            thread.setDaemon(true);
            return thread;
        });

        // Schedule cleanup task
        Runnable cleanupTask = () -> {
            try {
                SessionManager.getInstance().cleanupSessions();
            } catch (Exception e) {
                System.err.println("Error during scheduled session cleanup: " + e.getMessage());
                e.printStackTrace();
            }
        };

        // Run immediately on startup, then periodically
        scheduler.scheduleWithFixedDelay(
            cleanupTask,
            0, // Initial delay
            cleanupIntervalMinutes,
            TimeUnit.MINUTES
        );

        System.out.println("Session cleanup service started. Will run every " +
                         cleanupIntervalMinutes + " minutes.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Shutting down session cleanup service...");

        if (scheduler != null) {
            scheduler.shutdown();
            try {
                // Wait up to 10 seconds for existing tasks to finish
                if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                    // Wait another 5 seconds
                    if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                        System.err.println("Session cleanup scheduler did not terminate");
                    }
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Session cleanup service stopped.");
    }

    /**
     * Get environment variable or default value
     */
    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        value = dotenv.get(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        return defaultValue;
    }
}