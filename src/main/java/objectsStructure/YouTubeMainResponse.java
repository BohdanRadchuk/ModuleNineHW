package objectsStructure;

        import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

        import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YouTubeMainResponse {

    public ArrayList<Items> items = new ArrayList<>();
}
