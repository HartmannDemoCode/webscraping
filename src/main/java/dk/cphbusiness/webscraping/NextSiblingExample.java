package dk.cphbusiness.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NextSiblingExample {
        public static void main(String[] args) {
            String html = "<h3>headline 1</h3><p>Content 1</p><h3>headline 2</h3><p>Content 2</p>";

            // Parse the HTML content using Jsoup
            Document document = Jsoup.parse(html);

            // Select all <h3> elements
            Elements headlines = document.select("h3");

            // Get the text of the headline
            // Get the next sibling element (in this case, the <p> tag after the <h3>)
            // Get the text of the content
            headlines.forEach(headline -> {
                String headlineText = headline.text();
                Element content = headline.nextElementSibling();
                String contentText = content.text();
                System.out.println("Headline: " + headlineText);
                System.out.println("Content: " + contentText);
                System.out.println();
            });
        }
    }
