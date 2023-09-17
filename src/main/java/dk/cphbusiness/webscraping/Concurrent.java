package dk.cphbusiness.webscraping;

import dk.cphbusiness.webscraping.dto.ResultDTO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Concurrent {
    public static void main(String[] args) {
        // Create a list of URLs to scrape
        List<String> urls = List.of(
                "https://en.wikipedia.org/wiki/JavaScript"
                ,"https://en.wikipedia.org/wiki/Perl"
                ,"https://en.wikipedia.org/wiki/PHP"
                ,"https://en.wikipedia.org/wiki/SQL"
                ,"https://en.wikipedia.org/wiki/TypeScript"
                ,"https://en.wikipedia.org/wiki/Visual_Basic"
                ,"https://en.wikipedia.org/wiki/ML_(programming_language)"
                ,"https://en.wikipedia.org/wiki/COBOL"
                ,"https://en.wikipedia.org/wiki/ABAP"
                ,"https://en.wikipedia.org/wiki/PL/I"
                ,"https://en.wikipedia.org/wiki/ActionScript"
                ,"https://en.wikipedia.org/wiki/Smalltalk"
                ,"https://en.wikipedia.org/wiki/Visual_FoxPro");

        ExecutorService executorService = null;
        try {
            // Create a thread pool with an appropriate amount of threads
            executorService = java.util.concurrent.Executors.newFixedThreadPool(urls.size()/2);
            List<Future<ResultDTO>> futures = new ArrayList<>();

            for (String url : urls.subList(0,3)) {
                Future<ResultDTO> fut = executorService.submit(new ScraperTask(url));
                futures.add(fut);
            }
            for(Future<ResultDTO> fut : futures){
                ResultDTO resultDTO = fut.get();
                System.out.println(resultDTO);
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }

    }
    private static class ScraperTask implements Callable<ResultDTO> {
        private final String url;
        public ScraperTask(String url) {
            this.url = url;
        }
        @Override
        public ResultDTO call() throws Exception {
            // Scrape the URL
            ResultDTO content = Scraper.scrape(url);
            return content;
        }
    }
    private static class Scraper {
        public static ResultDTO scrape(String url){
            try {
                Document doc = Jsoup.connect(url).get();
                ResultDTO resultDTO = ResultDTO.builder()
                        .url(url)
                        .title(doc.title())
//                        .description(doc.select("meta[name=description]").first().attr("content"))
//                        .keywords(doc.select("meta[name=keywords]").first().attr("content"))
                        .content(doc.select("body").text())
                        .build();
                // Extract links:
                doc.select("a[href]").forEach(link -> resultDTO.addLink(link.attr("href")));
                // Extract images:
                doc.select("img[src]").forEach(image -> resultDTO.addImage(image.attr("src")));
                // Extract headlines:
                doc.select("h1, h2, h3, h4, h5, h6").forEach(headline -> resultDTO.addHeadline(headline.text()));
                return resultDTO;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        // Get the response from the URL (Old school)
        public static String getResponse(String url) {

            HttpClient httpClient = HttpClients.createDefault();
            try {
                HttpResponse response = httpClient.execute(new HttpGet(url));
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String htmlContent = EntityUtils.toString(entity, "UTF-8");
                    return htmlContent;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
    }
}
