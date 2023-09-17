package dk.cphbusiness.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleDemo {
    public static void main(String[] args) {
        String filePath = "demo.html";
        // Read html document from demo.html
        Document document = null;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
            String htmlDoc = lines.stream().collect(Collectors.joining("\n"));
            // Parse the html document through Jsoup
            document = Jsoup.parse(htmlDoc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Use CSS selectors to extract data
        Elements bookElements = document.select(".book"); // Select all elements with class "book"

        for (Element bookElement : bookElements) {
            String title = bookElement.select(".title").text(); // Select the title element
            String author = bookElement.select(".author").text(); // Select the author element
            String genre = bookElement.select(".genre").text(); // Select the genre element

            System.out.println("Title: " + title);
            System.out.println("Author: " + author);
            System.out.println("Genre: " + genre);
            System.out.println();
        }
    }

}
