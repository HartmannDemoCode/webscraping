package dk.cphbusiness.webscraping;

import com.google.gson.*;
import lombok.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class WeatherMap {
    // Show how to read Rest API Json into a DTO or a JsonObject
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        WeatherMap wm = new WeatherMap();
        wm.getResponseBody2("https://jsonplaceholder.typicode.com/todos/1");
        String key = System.getenv("WEATHER_MAP_KEY");
        GeoLocationDTO location = wm.getLocation("Lyngby", key); // Get location from city name. Format: [{"name":"Kongens Lyngby","local_names":{"lt":"Liungbiu","da":"Kongens Lyngby"},"lat":55.7718649,"lon":12.5051413,"country":"DK","state":"Capital Region of Denmark"}]

        // With DTOs:
        WeatherDataDTO wdd = wm.getCurrentWeather(location.getLat(), location.getLon(), key);
        System.out.println("Temperature: " + wdd.getMain().getTemp());
        System.out.println("Weather Description: " + wdd.getWeather()[0].getDescription());

        // With JsonObject:
        JsonObject locationWithJsonObject = wm.getWeatherWithJsonObject(location.getLat(), location.getLon(), key);

        System.out.println("Temperature from JsonObject: " + locationWithJsonObject.get("temperature"));
    }

    private String getResponseBody(String url) {
        // Using OkHttp: Can sometime cause program to hang. Then use Apache HttpClient instead.
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String res = response.body().string();
            System.out.println();
            return res;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String getResponseBody2(String url){
        // Alternative to OkHttpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String html = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(html);
            return html;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Using DTOs to represent the JSON structure:
    public GeoLocationDTO getLocation(String city, String key) {
        String geoLocatorUrl = "http://api.openweathermap.org/geo/1.0/direct?q={city}&limit=1&appid={API key}"
                .replace("{city}", city)
                .replace("{API key}", key);

        String res = getResponseBody(geoLocatorUrl);
        System.out.println("JSON Structure: " + res);
        GeoLocationDTO[] locations = gson.fromJson(res, GeoLocationDTO[].class);
        System.out.println(locations[0]);
        return locations[0];
    }

    // Using Gsons JsonObject and JsonArray to represent the JSON structure:
    public JsonObject getWeatherWithJsonObject(String lat, String lon, String key) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}"
                .replace("{lat}", lat)
                .replace("{lon}", lon)
                .replace("{API key}", key);
        String res = getResponseBody(url);
        System.out.println("JSON Structure: " + res);

        JsonElement jsonElement = JsonParser.parseString(res);
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObject toReturn = new JsonObject();

            // Access specific fields in the JSON
            double temperature = jsonObject
                    .getAsJsonObject("main")
                    .get("temp")
                    .getAsDouble();
            String weatherDescription = jsonObject
                    .getAsJsonArray("weather")
                    .get(0)
                    .getAsJsonObject()
                    .get("description")
                    .getAsString();
            String name = jsonObject
                    .get("name")
                    .getAsString();

            // Build the object to return
            toReturn.addProperty("temperature", temperature);
            toReturn.addProperty("weatherDescription", weatherDescription);
            toReturn.addProperty("name", name);
            return toReturn;
        } else {
            throw new RuntimeException("Not a JSON object");
        }
    }

    public WeatherDataDTO getCurrentWeather(String lat, String lon, String key) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}"
                .replace("{lat}", lat)
                .replace("{lon}", lon)
                .replace("{API key}", key);

        String res = getResponseBody(url);
        WeatherDataDTO weatherDataDTO = gson.fromJson(res, WeatherDataDTO.class);
        return weatherDataDTO;
    }

    @Getter
    @ToString
    private class GeoLocationDTO {
        private String lat;
        private String lon;
    }

    @Getter
    @ToString
    public class WeatherDataDTO {
        private Coord coord;
        private Weather[] weather;
        private String base;
        private Main main;
        private int visibility;
        private Wind wind;
        private long dt;
        private Sys sys;
        private int timezone;
        private long id;
        private String name;
        private int cod;

        // Nested classes for the JSON structure:
        @Getter
        @ToString
        public static class Coord {
            private double lon;
            private double lat;
        }

        @Getter
        @ToString
        public static class Weather {
            private int id;
            private String main;
            private String description;
            private String icon;
        }

        @Getter
        @ToString
        public static class Main {
            private double temp;
            private double feels_like;
            private double temp_min;
            private double temp_max;
            private int pressure;
            private int humidity;
        }

        @Getter
        @ToString
        public static class Wind {
            private double speed;
            private int deg;
        }

        @Getter
        @ToString
        public static class Sys {
            private int type;
            private int id;
            private String country;
            private long sunrise;
            private long sunset;
        }
    }

}
