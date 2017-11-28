package objectsStructure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.net.URL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Medium {
    public String url;
}
