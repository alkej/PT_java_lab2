package pl.edu.pg;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class PrimaryController {

    private File selectedFile;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button send_btn;

    @FXML
    private TextField ip_adres_fld;

    @FXML
    private Button choose_btn;

    @FXML
    private TextField file_name_fld;

    @FXML
    private TextField port_fld;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label statusLabel;

    @FXML
    void initialize() {
        assert send_btn != null : "fx:id=\"send_btn\" was not injected: check your FXML file 'primary.fxml'.";
        assert ip_adres_fld != null : "fx:id=\"ip_adres_fld\" was not injected: check your FXML file 'primary.fxml'.";
        assert choose_btn != null : "fx:id=\"choose_btn\" was not injected: check your FXML file 'primary.fxml'.";
        assert port_fld != null : "fx:id=\"port_fld\" was not injected: check your FXML file 'primary.fxml'.";
        assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'primary.fxml'.";
        assert statusLabel != null : "fx:id=\"statusLabel\" was not injected: check your FXML file 'primary.fxml'.";


        ip_adres_fld.setText("127.0.0.1");
        port_fld.setText("3939");

        choose_btn.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            selectedFile = fileChooser.showOpenDialog(choose_btn.getScene().getWindow());

            if (selectedFile != null){
                file_name_fld.setText(selectedFile.getName());
            }

        });

        send_btn.setOnAction(actionEvent -> {
            if (selectedFile != null){
               Task<Void> sendFileTask = new SendFileTask(selectedFile,
                       ip_adres_fld.getText(), Integer.parseInt(port_fld.getText())); //klasa zadania

                statusLabel.textProperty().bind(sendFileTask.messageProperty());
                progressBar.progressProperty().bind(sendFileTask.progressProperty());

                executor.submit(sendFileTask);
            }

        });


    }


}
