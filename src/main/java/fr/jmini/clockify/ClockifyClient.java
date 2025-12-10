package fr.jmini.clockify;

import java.util.List;

import fr.jmini.clockify.model.Project;
import fr.jmini.clockify.model.Tag;
import fr.jmini.clockify.model.TimeEntry;
import fr.jmini.clockify.model.User;

public interface ClockifyClient {

    User getCurrentUser();

    List<TimeEntry> getTimeEntries(String workspaceId, String userId, String fromParam, String toQueryParam);

    List<Project> getProjects(String workspaceId, String userId);

    List<Tag> getTags(String workspaceId, String userId);

}
