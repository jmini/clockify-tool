package fr.jmini.clockify.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "id",
        "description",
        "tagIds",
        "userId",
        "billable",
        "taskId",
        "projectId",
        "timeInterval",
        "workspaceId",
        "isLocked",
        "customFieldValues"
})
public class TimeEntry {

    @JsonProperty("id")
    private String id;

    @JsonProperty("description")
    private String description;

    @JsonProperty("tagIds")
    private String tagIds;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("billable")
    private boolean billable;

    @JsonProperty("taskId")
    private String taskId;

    @JsonProperty("projectId")
    private String projectId;

    @JsonProperty("timeInterval")
    private TimeInterval timeInterval;

    @JsonProperty("workspaceId")
    private String workspaceId;

    @JsonProperty("isLocked")
    private boolean isLocked;

    @JsonProperty("customFieldValues")
    private List<String> customFieldValues;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isBillable() {
        return billable;
    }

    public void setBillable(boolean billable) {
        this.billable = billable;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public List<String> getCustomFieldValues() {
        return customFieldValues;
    }

    public void setCustomFieldValues(List<String> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }
}
