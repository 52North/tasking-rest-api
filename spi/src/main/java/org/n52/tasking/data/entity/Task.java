package org.n52.tasking.data.entity;

public class Task {
    
    private String id;
    
    private String encodedParameters;
    
    private long estimatedToC;
    
    private String taskStatus;
    
    private String requestStatus;
    
    private long submittedAt;
    
    private double percentCompletion;
    
    private long updatedAt;
    
    private String resultId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEncodedParameters() {
        return encodedParameters;
    }

    public void setEncodedParameters(String encodedParameters) {
        this.encodedParameters = encodedParameters;
    }

    public long getEstimatedToC() {
        return estimatedToC;
    }

    public void setEstimatedToC(long estimatedToC) {
        this.estimatedToC = estimatedToC;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public long getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(long submittedAt) {
        this.submittedAt = submittedAt;
    }

    public double getPercentCompletion() {
        return percentCompletion;
    }

    public void setPercentCompletion(double percentCompletion) {
        this.percentCompletion = percentCompletion;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }
    
    
}
