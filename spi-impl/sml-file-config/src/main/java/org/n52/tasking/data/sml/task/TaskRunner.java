package org.n52.tasking.data.sml.task;

import org.n52.tasking.data.entity.Task;

interface TaskRunner {
    
    void runTask(Task task);
    
    void shutdown();
}
