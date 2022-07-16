module ProjectList {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires itextpdf;


    opens com.example.tutoforreal to javafx.fxml;
    exports com.example.tutoforreal;
}