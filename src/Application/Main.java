package Application;

import Dictionary.DictionaryManagement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    // tieng nghac khi mo app
    String pathMusic = "khoiDong.mp3";
    Media media = new Media(new File(pathMusic).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(media);

    @Override
    public void start(Stage primaryStage){
        try {

            // Nap du lieu tu file vao -> dictionary
            DictionaryManagement.insertFromFile();
            mediaPlayer.play();

            Parent root = FXMLLoader.load(getClass().getResource("FXML/mainUI.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("CSS/styleUI.css").toExternalForm());
            primaryStage.setTitle("Từ điển Anh - Việt");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
