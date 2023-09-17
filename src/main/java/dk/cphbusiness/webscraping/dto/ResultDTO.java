package dk.cphbusiness.webscraping.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class ResultDTO {
    private String url;
    private String title;
    private String description;
    private String keywords;
    private String content;
    private List<String> links = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private List<String> headlines = new ArrayList<>();

    @Builder
    public ResultDTO(String url, String title, String description, String keywords, String content) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.content = content;
    }
    public void addLink(String link) {
        this.links.add(link);
    }
    public void addImage(String image) {
        this.images.add(image);
    }
    public void addHeadline(String headline) {
        this.headlines.add(headline);
    }
}
