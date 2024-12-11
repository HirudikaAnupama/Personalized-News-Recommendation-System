module org.example.personalizednewsrecommendationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.opencsv;


    opens org.example.personalizednewsrecommendationsystem to javafx.fxml;
    exports org.example.personalizednewsrecommendationsystem;
    exports org.example.personalizednewsrecommendationsystem.ArticleManagement;
    opens org.example.personalizednewsrecommendationsystem.ArticleManagement to javafx.fxml;
    exports org.example.personalizednewsrecommendationsystem.GUIContollers;
    opens org.example.personalizednewsrecommendationsystem.GUIContollers to javafx.fxml;
    exports org.example.personalizednewsrecommendationsystem.ConcurrencyManagement;
    opens org.example.personalizednewsrecommendationsystem.ConcurrencyManagement to javafx.fxml;
    exports org.example.personalizednewsrecommendationsystem.DatabaseManagement;
    opens org.example.personalizednewsrecommendationsystem.DatabaseManagement to javafx.fxml;
    exports org.example.personalizednewsrecommendationsystem.UserManagement;
    opens org.example.personalizednewsrecommendationsystem.UserManagement to javafx.fxml;
    exports org.example.personalizednewsrecommendationsystem.RecommendationSystem;
    opens org.example.personalizednewsrecommendationsystem.RecommendationSystem to javafx.fxml;



}