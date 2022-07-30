package fr.jmini.clockify.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "id",
        "email",
        "name",
        "activeWorkspace",
        "defaultWorkspace"
})
public class User {

    @JsonProperty("id")
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("activeWorkspace")
    private String activeWorkspace;

    @JsonProperty("defaultWorkspace")
    private String defaultWorkspace;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActiveWorkspace() {
        return activeWorkspace;
    }

    public void setActiveWorkspace(String activeWorkspace) {
        this.activeWorkspace = activeWorkspace;
    }

    public String getDefaultWorkspace() {
        return defaultWorkspace;
    }

    public void setDefaultWorkspace(String defaultWorkspace) {
        this.defaultWorkspace = defaultWorkspace;
    }
}
