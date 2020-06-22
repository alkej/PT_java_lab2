module pl.edu.pg {
    requires javafx.controls;
    requires javafx.fxml;

    opens pl.edu.pg to javafx.fxml;
    exports pl.edu.pg;
}