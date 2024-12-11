package org.example.personalizednewsrecommendationsystem.ArticleManagement;

import org.example.personalizednewsrecommendationsystem.DatabaseManagement.DatabaseManagement;


public class ArticleManagement {
    private final String articleCategory;
    private final String articleHeadline;
    private final String articleDescription;
    private final String articleImagePath;

    private final DatabaseManagement databaseManagement;  // DatabaseManagement object to interact with the database


    // Constructor to initialize article fields
    public ArticleManagement(String category, String headline, String description, String imagePath, DatabaseManagement databaseManagement) {
        this.articleCategory = category;
        this.articleHeadline = headline;
        this.articleDescription = description;
        this.articleImagePath = imagePath;
        this.databaseManagement = databaseManagement;
    }



    // Saves the current article to the database
    public void storeNewsArticle() {
        if (databaseManagement != null) {
            databaseManagement.insertArticle(articleHeadline, articleDescription, articleCategory, articleImagePath);

        } else {
            System.err.println("DatabaseManagement instance is not set. Unable to save article.");
        }
    }

}
