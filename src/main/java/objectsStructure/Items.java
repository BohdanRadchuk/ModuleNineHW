package objectsStructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Items {
    public ContentDetails contentDetails;
    public Snippet snippet;
}
