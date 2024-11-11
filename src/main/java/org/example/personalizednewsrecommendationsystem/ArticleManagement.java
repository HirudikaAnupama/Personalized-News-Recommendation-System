package org.example.personalizednewsrecommendationsystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ArticleManagement {

    private static final String CSV_PATH = "C:\\Users\\LENOVO\\Desktop\\archive (2)\\NewsCategorizer.csv";
    private final DataBaseManagement dbManager = new DataBaseManagement();

    // Method to load articles from CSV and insert them into the database
    public void loadArticlesFromCsv() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 5) {
                    String category = values[0];
                    String headline = values[1];
                    String link = values[2];
                    String shortDescription = values[3];
                    String keywords = values[4];

                    // Insert each article into the database
                    dbManager.insertArticle(category, headline, link, shortDescription, keywords);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading articles from CSV: " + e.getMessage());
        }
    }
}
