
package org.example.personalizednewsrecommendationsystem;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;

public class ArticleManagement {

    private static final String CSV_PATH = "C:\\Users\\LENOVO\\Desktop\\archive (2)\\NewsCategorizer.csv";
    private final DataBaseManagement dbManager = new DataBaseManagement();

    public ArticleManagement() {
    }

    // Main method to load articles from the CSV
    public void loadArticlesFromCsv() {
        int rowCount = 0;
        try (CSVReader csvReader = new CSVReader(new FileReader(CSV_PATH))) {
            csvReader.readNext(); // Skip the header row

            String[] values;
            while ((values = csvReader.readNext()) != null) {
                rowCount++;
                processCsvRow(rowCount, values);
            }

            System.out.println("Total rows processed: " + rowCount);

        } catch (IOException | CsvValidationException e) {
            System.out.println("Error loading articles from CSV: " + e.getMessage());
        }
    }

    // Method to process each row from the CSV
    private void processCsvRow(int rowCount, String[] values) {
        System.out.println("Row " + rowCount + " has " + values.length + " columns.");

        // Ensure the row has exactly 2 columns (headline and shortDescription)
        if (values.length == 2) {
            String headline = values[0].trim();
            String shortDescription = values[1].trim();

            // Debug log: show the data being processed
            System.out.println("Processing row " + rowCount + ": " + headline + " | " + shortDescription);

            validateAndInsertArticle(headline, shortDescription);
        } else {
            System.out.println("Skipping row " + rowCount + ": Insufficient data (expected 2 columns).");
        }
    }

    // Method to validate, categorize, and insert an article into the database
    private void validateAndInsertArticle(String headline, String shortDescription) {
        if (headline.isEmpty() || shortDescription.isEmpty()) {
            System.out.println("Skipping insertion: Empty headline or description.");
            return;
        }

        // Categorize article using NLP (simple keyword matching)
        String category = categorizeArticle(shortDescription);

        // Debug log: show data before insertion
        System.out.println("Inserting article: " + headline + " | " + shortDescription + " | Category: " + category);

        // Insert the article into the database
        dbManager.insertArticle(headline, shortDescription, category);
    }

    // Simple NLP-based method to categorize the article based on keywords
    // Simple NLP-based method to categorize the article based on keywords
    private String categorizeArticle(String text) {
        text = text.toLowerCase();

        // Define keywords for each category
        String[] technologyKeywords = {
                "technology", "software", "internet", "computer", "ai", "artificial intelligence", "programming", "gadget",
                "hardware", "machine learning", "big data", "cloud computing", "data science", "blockchain", "cybersecurity",
                "innovation", "electronics", "automation", "virtual reality", "VR", "AR", "robotics", "IT", "5G", "startup"
        };

        String[] healthKeywords = {
                "health", "medicine", "wellness", "fitness", "disease", "nutrition", "hospital", "doctor",
                "mental health", "public health", "surgery", "therapy", "vaccine", "virus", "epidemic", "pharmacy",
                "cardiology", "diet", "exercise", "healthcare", "clinic", "epidemiology", "immunization", "diagnosis",
                "symptom", "infection", "prevention", "biotechnology"
        };

        String[] sportsKeywords = {
                "sports", "game", "athlete", "football", "soccer", "basketball", "olympics", "tournament",
                "cricket", "tennis", "rugby", "golf", "baseball", "hockey", "league", "athletics", "marathon",
                "medal", "championship", "score", "goal", "team", "competition", "world cup", "boxing", "swimming",
                "cycling", "FIFA", "UEFA", "NBA", "Premier League", "Super Bowl"
        };

        String[] politicsKeywords = {
                "politics", "election", "government", "policy", "diplomacy", "president", "congress", "senate",
                "minister", "parliament", "democracy", "republic", "campaign", "law", "constitution", "international relations",
                "embassy", "treaty", "UN", "NATO", "alliance", "legislation", "bill", "party", "leader", "conservative",
                "liberal", "socialist", "reform", "voter", "referendum", "ballot", "governor", "ambassador"
        };

        String[] financeKeywords = {
                "finance", "economy", "stock", "market", "investment", "bank", "cryptocurrency", "trade",
                "budget", "tax", "income", "revenue", "insurance", "loan", "mortgage", "interest rate", "fund", "credit",
                "forex", "capital", "inflation", "deflation", "gross domestic product", "GDP", "dividend", "debt",
                "bond", "wealth", "accounting", "audit", "spending", "venture capital", "IPO", "Wall Street", "hedge fund"
        };

        String[] entertainmentKeywords = {
                "movie", "music", "entertainment", "film", "celebrity", "hollywood", "award", "show",
                "theater", "comedy", "drama", "TV", "series", "album", "song", "concert", "actor", "actress",
                "director", "producer", "streaming", "Netflix", "cinema", "festival", "reality show", "Bollywood",
                "Broadway", "Oscars", "Emmy", "Grammy", "media", "binge-watch", "fan", "celebration", "viral"
        };

        // Count keyword matches for each category
        int techCount = countKeywordMatches(text, technologyKeywords);
        int healthCount = countKeywordMatches(text, healthKeywords);
        int sportsCount = countKeywordMatches(text, sportsKeywords);
        int politicsCount = countKeywordMatches(text, politicsKeywords);
        int financeCount = countKeywordMatches(text, financeKeywords);
        int entertainmentCount = countKeywordMatches(text, entertainmentKeywords);

        // Determine the category with the highest match count
        int maxCount = Math.max(techCount, Math.max(healthCount, Math.max(sportsCount, Math.max(politicsCount, Math.max(financeCount, entertainmentCount)))));

        if (maxCount == 0) {
            return "General"; // Default category if no keywords match
        } else if (maxCount == techCount) {
            return "Technology";
        } else if (maxCount == healthCount) {
            return "Health";
        } else if (maxCount == sportsCount) {
            return "Sports";
        } else if (maxCount == politicsCount) {
            return "Politics";
        } else if (maxCount == financeCount) {
            return "Finance";
        } else {
            return "Entertainment";
        }
    }

    // Helper method to count matches for a given set of keywords
    private int countKeywordMatches(String text, String[] keywords) {
        int count = 0;
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                count++;
            }
        }
        return count;
    }

}
