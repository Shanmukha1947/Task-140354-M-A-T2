package com.example.task140354mat2;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DynamicRateLimiterWebScraper {

    private static final int INITIAL_RATE_LIMIT_MS = 100; // Initial rate limit in milliseconds
    private static final int MAX_RATE_LIMIT_MS = 1000; // Maximum rate limit in milliseconds
    private static final int MIN_RATE_LIMIT_MS = 10; // Minimum rate limit in milliseconds
    private static final int RATE_LIMIT_ADJUSTMENT_FACTOR = 2; // Factor to adjust rate limit

    private long rateLimitMs = INITIAL_RATE_LIMIT_MS;

    public void scrapeWebPage(String urlString) throws IOException {

        while (true) {

            long startTime = System.nanoTime();

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                // Read the response if needed
                // ...

            } catch (IOException e) {
                e.printStackTrace();
            }

            long endTime = System.nanoTime();
            long executionTimeMs = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);

            adjustRateLimit(executionTimeMs);

            // Wait for the adjusted rate limit period before sending the next request
            try {
                Thread.sleep(rateLimitMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void adjustRateLimit(long executionTimeMs) {

        if (executionTimeMs > rateLimitMs) {
            // Server response was slower, increase the rate limit to avoid overloading the server
            rateLimitMs = Math.min(rateLimitMs * RATE_LIMIT_ADJUSTMENT_FACTOR, MAX_RATE_LIMIT_MS);
        } else if (executionTimeMs < rateLimitMs / RATE_LIMIT_ADJUSTMENT_FACTOR) {
            // Server response was faster, decrease the rate limit to conserve resources
            rateLimitMs = Math.max(rateLimitMs / RATE_LIMIT_ADJUSTMENT_FACTOR, MIN_RATE_LIMIT_MS);
        }

        System.out.println("Adjusted rate limit: " + rateLimitMs + " ms");
    }

    public static void main(String[] args) throws IOException {
        DynamicRateLimiterWebScraper scraper = new DynamicRateLimiterWebScraper();
        scraper.scrapeWebPage("https://example.com"); // Replace with the URL you want to scrape
    }
}
