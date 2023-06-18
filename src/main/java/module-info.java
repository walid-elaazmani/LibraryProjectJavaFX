module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;

    opens javafx to javafx.fxml;
    exports javafx;
    exports model;
    opens model to javafx.fxml;
}