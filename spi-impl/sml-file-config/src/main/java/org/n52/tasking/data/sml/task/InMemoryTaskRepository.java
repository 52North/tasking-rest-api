package org.n52.tasking.data.sml.task;

import java.util.List;
import org.n52.tasking.data.entity.Task;
import org.n52.tasking.data.repository.TaskRepository;

public class InMemoryTaskRepository implements TaskRepository {

    @Override
    public List<Task> getTasks() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Task> getTasks(String deviceId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean hasTask(String taskId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Task getTask(String taskId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
