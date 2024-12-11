package org.example.personalizednewsrecommendationsystem.ConcurrencyManagement;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrencyManagement {
    private final ExecutorService executorService;


    // Initializes the thread pool with a fixed number of threads.
    public ConcurrencyManagement(int maxConcurrentTasks) {
        this.executorService = Executors.newFixedThreadPool(maxConcurrentTasks);
    }

    // Submits a task for execution.
    public void handleTask(Runnable task) {
        executorService.submit(task);
    }


    // Returns the executor service instance.
    public ExecutorService getExecutorService() {
        return this.executorService;
    }


    // Shuts down the executor service.
    public void shutdown() {
        executorService.shutdown();
    }
}


