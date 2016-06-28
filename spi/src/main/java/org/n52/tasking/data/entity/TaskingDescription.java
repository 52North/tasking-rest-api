package org.n52.tasking.data.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TaskingDescription {
    
    private final Map<String, Parameter> taskingParameters;
    
    public TaskingDescription() {
        this.taskingParameters = new HashMap<>();
    }

    public Map<String, Parameter> getTaskingParameters() {
        return Collections.unmodifiableMap(taskingParameters);
    }
    
    public TaskingDescription addParameter(Parameter parameter) {
        taskingParameters.put(parameter.getName(), parameter);
        return this;
    } 

}
