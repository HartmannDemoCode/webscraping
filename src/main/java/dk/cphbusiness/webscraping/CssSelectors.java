package dk.cphbusiness.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CssSelectors{
    public static void main(String[] args) {
        String html = "<html><body>" +
                "<div class='container'>" +
                "   <h1 id='title'>Hello, World!</h1>" +
                "   <ul>" +
                "       <li>Item 1</li>" +
                "       <li>Item 2</li>" +
                "   </ul>" +
                "   <p class='info'>This is some information.</p>" +
                "</div></body></html>";

        Document document = Jsoup.parse(html);

        // Tag Selector
        Elements divElements = document.select("div");
        System.out.println("Div Elements: " + divElements);

        // Class Selector
        Elements infoParagraphs = document.select(".info");
        System.out.println("Info Paragraphs: " + infoParagraphs);

        // ID Selector
        Element titleElement = document.select("#title").first();
        System.out.println("Title Element: " + titleElement.text());

        // Descendant Selector
        Elements listItems = document.select("div ul li");
        System.out.println("List Items: " + listItems);

        // Child Selector
        Elements listItemsDirectChildren = document.select("div > ul > li");
        System.out.println("List Items (Direct Children): " + listItemsDirectChildren);

        // Attribute Selector
        Element anchorWithHref = document.select("a[href]").first();
        System.out.println("Anchor with Href: " + anchorWithHref);

        // Pseudo-Class Selector (e.g., :first-child)
        Element firstListItem = document.select("li:first-child").first();
        System.out.println("First List Item: " + firstListItem.text());
    }
}

