package dk.cphbusiness.webscraping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrustPilotEx {
    public static void main(String[] args) {
        TrustPilotEx tp = new TrustPilotEx();
        String url = "https://www.trustpilot.com/review/www.apple.com?page=#";
        String xPath = "/html/body/div[1]/div/div/main/div/div[4]/section/div[31]/nav/a[6]/span"; // For getting number of pages;
        String cssReviewSelector = "div.styles_cardWrapper__LcCPA.styles_show__HUXRb.styles_reviewCard__9HxJJ"; // For getting reviews

        int NUMBER_OF_PAGES = Integer.parseInt(tp.getElements(url, xPath, true).text());
        System.out.println("The company has " + NUMBER_OF_PAGES + " pages of reviews");

        Elements reviewElements = tp.getElements(url.replace("#","1"), cssReviewSelector);
//        System.out.println("First review: \n"+reviewElements.get(0).html()+"\n");

        List<TrustPilotReviewDTO> reviews = tp.getReviews(reviewElements);
        reviews.forEach(r-> System.out.println("REVIEW: \n"+r+"\n"));
    }
    private List<TrustPilotReviewDTO> readPaginated(String url, int pages, String cssReviewSelector){
        List<TrustPilotReviewDTO> reviews = new ArrayList<>();
        for(int i = 1; i <= pages; i++){
            Elements reviewElements = getElements(url.replace("#", String.valueOf(i)), cssReviewSelector);
            List<TrustPilotReviewDTO> pageReviews = getReviews(reviewElements);
            reviews.addAll(pageReviews);
        }
        return reviews;
    }

    private Elements getElements(String url, String cssReviewSelector) {
        return getElements(url.replace("#", "1"), cssReviewSelector, false);
    }

    public List<TrustPilotReviewDTO> getReviews(Elements reviewElements) {
//        reviewElements.stream().peek(e -> System.out.println("NEW ELEMENT: \n"+e.html())).toList();
        return reviewElements
                .stream()
//                .peek(e -> System.out.println("PEEK: "+e.html()))
                .map(this::getReview)
                .toList();
    }

    public TrustPilotReviewDTO getReview(Element element) {
        TrustPilotReviewDTO reviewDTO = TrustPilotReviewDTO.builder()
                .title(element.select("h2").text())
                .review(element.select("p.typography_body-l__KUYFJ").text())
                .stars(Integer.parseInt(element.select("div.star-rating_starRating__4rrcf img").attr("alt").substring(6, 7)))
                .date(element.select("time").text())
                .author(element.select("div.styles_consumerDetailsWrapper__p2wdr > a > span").text())
                .numberOfReviews(Integer.parseInt(element.select("div.styles_consumerExtraDetails__fxS4S span.typography_body-m__xgxZ_").text().substring(0, 1)))
                .country(element.select("svg.icon_icon__ECGRL").get(0).nextElementSibling()==null?"No country":element.select("svg.icon_icon__ECGRL").get(0).nextElementSibling().text())
//                .reply(element.select("div.review-content-header__reply").text())
//                .replyDate(element.select("div.review-content-header__reply-date").text())
                .build();
        return reviewDTO;
    }


    public Elements getElements(String url, String selector, boolean isXpath) {
        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                            "(KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36")
                    .get();
            if (isXpath)
                return document.selectXpath(selector);
            else
                return document.select(selector);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    private static class TrustPilotReviewDTO {
        private String title;
        private String review;
        private int stars;
        private String date;
        private String author;
        private int numberOfReviews;
        private String country;
//        private String reply;
//        private String replyDate;
    }
}
