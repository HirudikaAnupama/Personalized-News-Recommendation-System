module org.example.personalizednewsrecommendationsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.personalizednewsrecommendationsystem to javafx.fxml;
    exports org.example.personalizednewsrecommendationsystem;
}