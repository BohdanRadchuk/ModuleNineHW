import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import objectsStructure.YouTubeMainResponse;

import java.awt.print.Book;
import java.io.IOException;

public class YouTubeApi {
    private final static String YOUTUBE_ADRESS = "https://www.googleapis.com/youtube/v3/activities";
    private final static String MY_API_KEY = "AIzaSyCl0nW-_pC68yOaRAvktYuByEKxYITPYNk";

    static {
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public static HttpResponse<YouTubeMainResponse> youTubeApiWork(String channelID, int maxResults) {
        try {
            return Unirest.get(YOUTUBE_ADRESS)
                    .queryString("channelId", channelID)
                    .queryString("maxResults", maxResults)
                    .queryString("part", "snippet,contentDetails")
                    //.queryString("snippet.type","upload")            -- пытался сделать что бы ютуб возвращал только активности у которых тип = аплоад
                    .queryString("key", MY_API_KEY)
                    .asObject(YouTubeMainResponse.class);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpResponse<YouTubeMainResponse> youTubeApiWork(String channelID) {
        return youTubeApiWork(channelID, 1);
    }
}
