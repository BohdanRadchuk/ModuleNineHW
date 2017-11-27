package objectsStructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Snippet {
    public String publishedAt;
    public String channelId;
    public String title;
    public String channelTitle;
    public Thumbnails thumbnails;
}
