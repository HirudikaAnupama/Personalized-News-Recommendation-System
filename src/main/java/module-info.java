module org.example.personalizednewsrecommendationsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.opencsv;


    opens org.example.personalizednewsrecommendationsystem to javafx.fxml;
    exports org.example.personalizednewsrecommendationsystem;
}