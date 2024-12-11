package org.example.personalizednewsrecommendationsystem.ArticleManagement;

import org.example.personalizednewsrecommendationsystem.ConcurrencyManagement.ConcurrencyManagement;
import org.example.personalizednewsrecommendationsystem.DatabaseManagement.DatabaseManagement;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArticleFetcher {

    // Path to the CSV file containing article data
    private static final String CSV_PATH = "src/main/resources/NewsArticles.csv";



    // Reads articles from the CSV file and saves valid ones to the database
    public void loadArticlesFromCSV() {
        try (CSVReader csvReader = new CSVReader(new FileReader(CSV_PATH))) {
            csvReader.readNext(); // Skip the header row
            List<String[]> rows = new ArrayList<>();
            String[] values;

            // Read all rows from the CSV file
            while ((values = csvReader.readNext()) != null) {
                rows.add(values);
            }
            // Process the rows concurrently
            processArticleRows(rows);

        } catch (IOException | CsvValidationException e) {
            // Print an error message if an exception occurs during processing
            System.err.println("Error loading articles from CSV: " + e.getMessage());
        }
    }




    // Processes CSV file rows to extract headline, description, and image path, and call categorization method
    private void processArticleRows(List<String[]> rows) {
        List<String> descriptions = new ArrayList<>();  // List to store descriptions for categorization
        List<String[]> validRows = new ArrayList<>();  // List to store valid rows from the CSV file

        // Iterate through each row in the CSV data
        for (String[] values : rows) {
            if (values.length == 3 && !values[0].trim().isEmpty() && !values[1].trim().isEmpty()) {
                validRows.add(values); // Add valid rows
                descriptions.add(values[1].trim()); // Extract descriptions for categorization
            }
        }
        if (!descriptions.isEmpty()) {
            ConcurrencyManagement concurrencyManagement = new ConcurrencyManagement(10);
            ArticleCategory categorize = new ArticleCategory(concurrencyManagement);
            DatabaseManagement databaseManagement = new DatabaseManagement();

            // Categorize articles concurrently
            Map<String, String> categorizedDescriptions = categorize.handleCategorizationConcurrency(descriptions);

            // Iterate through valid rows and store categorized articles in the database
            for (String[] row : validRows) {
                String headline = row[0].trim();  // Extract and trim the headline
                String description = row[1].trim();
                String imagePath = row[2].trim();
                String category = categorizedDescriptions.get(description); // Retrieve category

                // Create and save an ArticleManagement object
                ArticleManagement article = new ArticleManagement(category, headline, description, imagePath, databaseManagement);
                // Store the article in the database
                article.storeNewsArticle();
            }
        }
    }
}
