package org.n52.tasking.data.sml.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.n52.tasking.data.RequestStatus;
import org.n52.tasking.data.TaskStatus;
import org.n52.tasking.data.cmd.CreateTask;
import org.n52.tasking.data.entity.Task;
import org.n52.tasking.data.repository.TaskRepository;

public class InMemoryTaskRepository implements TaskRepository {

    private final Map<String, List<Task>> tasksByDevice;

    public InMemoryTaskRepository() {
        this.tasksByDevice = new HashMap<>();
    }

    @Override
    public Task createTask(CreateTask createTask) {
        Task task = new Task();
        task.setId(createTask.getId());
        task.setEncodedParameters(createTask.getParameters());
        task.setPercentCompletion(100.0);
        task.setTaskStatus(TaskStatus.FINISHED.name());
        task.setRequestStatus(RequestStatus.ACCEPTED.name());
        return task;
    }
    
    @Override
    public List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        tasksByDevice.values().stream().forEach((tasks) -> {
            list.addAll(tasks);
        });
        return Collections.unmodifiableList(list);
    }

    @Override
    public List<Task> getTasks(String deviceId) {
        List<Task> list = new ArrayList<>();
        tasksByDevice.get(deviceId).stream().forEach((task) -> {
            list.add(task);
        });
        return Collections.unmodifiableList(list);
    }

    @Override
    public boolean hasTask(String taskId) {
        return getTasks().stream().anyMatch(t -> t.getId().equals(taskId));
    }

    @Override
    public Task getTask(String taskId) {
        return getTasks().stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .get();
    }

}
