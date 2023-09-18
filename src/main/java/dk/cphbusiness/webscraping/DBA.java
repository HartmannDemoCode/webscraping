package dk.cphbusiness.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.ArrayList;

public class DBA {
    public static void main(String[] args) {
//        new DBA().getDBA();
        new DBA().getDBAPaginated();
    }
    public void getDBA(){

        // Fetch the page content using Jsoup
        try {
            String url = "https://www.dba.dk/soeg/?soeg=cykel";
            Document document = Jsoup.connect(url).get();
            Elements els = document.selectXpath("/html/body/div[4]/div[1]/div[1]/section/table/tbody/tr[4]/td[2]/a/span[1]");
            els.forEach(el -> System.out.println(el.text()));
            String cssSelectors = "ul.srp-tabs > li:nth-child(1) > div:nth-child(1) > span:nth-child(1) > small:nth-child(1)";
            String cssPath = "html.js.no-touch.history.rgba.multiplebgs.borderradius.boxshadow.textshadow.opacity.cssgradients.csstransitions.generatedcontent.localstorage.boxsizing.filereader.fileinput.placeholder.feoixmesr body.fixed div#page div#content.container div.sidebar-layout section.content.sidebar-left ul.srp-tabs li.current div.a span small";
            System.out.println("CSS Selectors: "  );
            document.select(cssSelectors).forEach(el -> System.out.println(el.text()));
            System.out.println("CSS Path: "  );
            document.select(cssPath).forEach(el -> System.out.println(el.text()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void getDBAPaginated(){
       String url = "https://www.dba.dk/soeg/side-#/?soeg=cykel&sort=price-desc&pris=(2000-4999)";
       int pages = 10;
        List<String> placements = new ArrayList();
         for (int i = 1; i <= pages; i++) {
              try {
                Document document = Jsoup.connect(url.replace("#", String.valueOf(i))).get();
                Elements contentTables = document.selectXpath("/html/body/div[4]/div[1]/div[1]/section/table");
                contentTables.forEach(el -> el.select("tr.dbaListing td.mainContent").forEach(td -> {
                    placements.add(td.text());
                }));
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
         }
         // Show only those posts containing the word "cykel".
         placements.stream().filter(s -> s.contains("cykel")).limit(10).forEach(System.out::println);

         // Show only those posts containing the word "dame cykel" using regex.
         placements.stream().filter(s -> s.matches("(?i).*dame.*cykel.*")).limit(10).forEach(System.out::println);
    }
}
