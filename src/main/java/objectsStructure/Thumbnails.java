package objectsStructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Thumbnails  {
    public String channelTitle;
    public Medium medium;

}
