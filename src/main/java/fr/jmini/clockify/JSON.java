package fr.jmini.clockify;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.jmini.clockify.model.Tag;
import fr.jmini.clockify.model.TimeEntry;
import fr.jmini.clockify.model.User;

public class JSON {

    private static final ObjectMapper objectMapper = createMapper();

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public static String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    public static User deserializeUser(String content) {
        try {
            return objectMapper.readValue(content, User.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Can not deserialize User", e);
        }
    }

    public static List<TimeEntry> deserializeTimeEntries(String content) {
        try {
            return objectMapper.readValue(content, objectMapper.getTypeFactory()
                    .constructCollectionLikeType(List.class, TimeEntry.class));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Can not deserialize TimeEntries", e);
        }
    }

    public static List<Tag> deserializeTags(String content) {
        try {
            return objectMapper.readValue(content, objectMapper.getTypeFactory()
                    .constructCollectionLikeType(List.class, Tag.class));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Can not deserialize Tag", e);
        }
    }

    private JSON() {
    }
}
