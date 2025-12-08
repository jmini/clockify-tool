package fr.jmini.clockify;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import fr.jmini.clockify.model.Tag;
import fr.jmini.clockify.model.TimeEntry;
import fr.jmini.clockify.model.User;

public class ClockifyHttpClient implements ClockifyClient {

    private String apiKey;

    public ClockifyHttpClient(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public User getCurrentUser() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.clockify.me/api/v1/user"))
                    .headers("X-Api-Key", apiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build()
                    .send(request, BodyHandlers.ofString());
            String content = response.body();
            return JSON.deserializeUser(content);
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException("Could not get the user", e);
        } catch (InterruptedException e) {
            Thread.currentThread()
                    .interrupt();
            throw new IllegalStateException("Could not get the user", e);
        }
    }

    @Override
    public List<TimeEntry> getTimeEntries(String workspaceId, String userId, String fromQueryParam, String toQueryParam) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.clockify.me/api/v1/workspaces/" + workspaceId + "/user/" + userId + "/time-entries?start=" + fromQueryParam + "&end=" + toQueryParam + "&page-size=5000"))
                    .headers("X-Api-Key", apiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build()
                    .send(request, BodyHandlers.ofString());
            String content = response.body();
            return JSON.deserializeTimeEntries(content);
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException("Could not get time entries", e);
        } catch (InterruptedException e) {
            Thread.currentThread()
                    .interrupt();
            throw new IllegalStateException("Could not get time entries", e);
        }
    }

    @Override
    public List<Tag> getTags(String workspaceId, String userId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.clockify.me/api/v1/workspaces/" + workspaceId + "/tags?page-size=5000"))
                    .headers("X-Api-Key", apiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build()
                    .send(request, BodyHandlers.ofString());
            String content = response.body();
            return JSON.deserializeTags(content);
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException("Could not get time entries", e);
        } catch (InterruptedException e) {
            Thread.currentThread()
                    .interrupt();
            throw new IllegalStateException("Could not get time entries", e);
        }
    }
}
