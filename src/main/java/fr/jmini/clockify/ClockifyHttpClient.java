package fr.jmini.clockify;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.jmini.clockify.model.TimeEntry;
import fr.jmini.clockify.model.User;

public class ClockifyHttpClient implements ClockifyClient {

    private String apiKey;
    private ObjectMapper objectMapper;

    public ClockifyHttpClient(String apiKey) {
        this.apiKey = apiKey;
        this.objectMapper = createMapper();
    }

    private ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
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
            return objectMapper.readValue(response.body(), User.class);
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
            return objectMapper.readValue(response.body(), objectMapper.getTypeFactory()
                    .constructCollectionLikeType(List.class, TimeEntry.class));
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException("Could not get time entries", e);
        } catch (InterruptedException e) {
            Thread.currentThread()
                    .interrupt();
            throw new IllegalStateException("Could not get time entries", e);
        }
    }
}
