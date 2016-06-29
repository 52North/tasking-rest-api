package org.n52.tasking.data.sml.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.n52.tasking.core.service.TaskService;
import org.n52.tasking.data.TaskStatus;
import org.n52.tasking.data.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmlFileConfigTaskRunner implements TaskRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void runTask(Task task) {
        task.setSubmittedAt(System.currentTimeMillis());
        task.setTaskStatus(TaskStatus.RUNNING.name());
        task.setPercentCompletion(0.0);
        
        executorService.execute(() -> {
            LOGGER.debug("Running task '{}' (parameters: '{}')", task.getId(), task.getEncodedParameters());

            // TODO change config in sml file
            
            task.setTaskStatus(TaskStatus.FINISHED.name());
            task.setPercentCompletion(100.0);
        });
    }

    @Override
    public void shutdown() {
        LOGGER.info("Shut down task runner. Stopping all running tasks.");
        this.executorService.shutdownNow();
    }

}
