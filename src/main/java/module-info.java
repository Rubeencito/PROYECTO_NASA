module com.example.nasa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.nasa to javafx.fxml;
    exports com.example.nasa;
}