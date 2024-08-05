/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package org.example.newsparserapplication.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.newsparserapplication.news.News;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * The NewsService class provides methods to interact with an external news API.
 * It fetches news articles based on their posted date using HTTP requests and
 * maps the JSON response to a list of News objects.
 */
public class NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);
    private static final String API_URL = "http://localhost:8081/news-by-date";

    /**
     * Retrieves news articles posted on a specific date.
     * This method sends an HTTP GET request to the API endpoint with the specified date as a query parameter,
     * parses the JSON response into a list of News objects, and returns it wrapped in an Optional.
     * If an error occurs during the process, an empty Optional is returned.
     *
     * @param date the date for which to fetch the news articles. Example: 2024-08-01
     * @return an Optional containing a list of News objects if the request is successful, otherwise an empty Optional
     */
    public Optional<List<News>> getNewsByPostedDate(LocalDate date) {
        try (HttpClient client = HttpClient.newHttpClient()){

            String dateString = date.toString();

            String urlWithParams = STR."\{API_URL}?date=\{URLEncoder.encode(dateString, StandardCharsets.UTF_8)}";

            HttpRequest request = HttpRequest.newBuilder()
                                             .uri(new URI(urlWithParams))
                                             .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            List<News> newsList = objectMapper.readValue(response.body(), new TypeReference<>() {
            });

            return Optional.of(newsList);
        }
        catch (Exception e) {
           logger.error(e.getMessage());
        }
        return Optional.empty();
    }

    

}
