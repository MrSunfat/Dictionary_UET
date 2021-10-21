package Application;

import Dictionary.Dictionary;
import Dictionary.DictionaryManagement;
import Dictionary.Word;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;


public class ControllerMain implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private HBox speek;
    @FXML
    private HBox denification;
    @FXML
    private HBox titleApp;

    @FXML
    private TextField inputWord;

    @FXML
    private Label wordTarget;

    @FXML
    private Label wordExplain;

    // tao ra 1 list tu goi y
    @FXML
    private ListView<String> wordList = new ListView<String>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Text icon = GlyphsDude.createIcon(FontAwesomeIcons.NAVICON, "24px");
        icon.setId("nav");
        Label denificationTitle = new Label("Denification");
        denificationTitle.setId("denificationTitle");
        denification.getChildren().add(icon);
        denification.getChildren().add(denificationTitle);

        Text logo = GlyphsDude.createIcon(FontAwesomeIcons.BOOKMARK, "20px");
        logo.setId("logo");
        Label titleHead = new Label("Advance English Dictionary");
        titleHead.setId("titleHead");
        titleApp.getChildren().add(logo);
        titleApp.getChildren().add(titleHead);

        speek.getChildren().add(GlyphsDude.createIcon(FontAwesomeIcons.VOLUME_UP, "24px"));

        // bat su kien khi nhap tu tim kiem
        inputWord.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            try {
                // xoa het items of wordList trước
                wordList.getItems().clear();

                // lay ra tu vung tu Dictionary.DictionaryManagement
                Dictionary dictionary = DictionaryManagement.getDictionary();

                // hien thi: goi y tu se tim
                for (int i = 0; i < dictionary.getWords().size(); i++) {
                    Word element = dictionary.getWords().get(i);

                    // kiem tra newValue co ton tai trong element.getWord_target()
                    // va kiem tra element.getWord_target() co bat dau = newValue
                    if(element.getWord_target().toUpperCase().contains(trimWord(newValue).toUpperCase())
                            && element.getWord_target().toUpperCase().indexOf(trimWord(newValue).toUpperCase()) == 0) {
                        wordList.getItems().add(element.getWord_target());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        // xu ly event: nhan chuot vao tu vung duoc goi y
        wordList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                        Dictionary list = DictionaryManagement.getDictionary();
                        int dictionarySize = list.getWords().size();

                        // tim vi tri cua "tu goi y" trong tu dien
                        int indexWord = Dictionary.binarySearch(list, 0, dictionarySize - 1, newValue);
                        Word word = list.getWords().get(indexWord);
                        inputWord.setText(word.getWord_target());
                        wordTarget.setText(word.getWord_target());
                        wordExplain.setText(word.getWord_explain());
                    }
                }
        );

        // nut nhan -> phat am tu vung
        speek.setOnMouseClicked(mouseEvent -> {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            Voice voice = VoiceManager.getInstance().getVoice("kevin");

            String inputWordText = trimWord(wordTarget.getText());

            if(voice != null) {
                voice.allocate();
                voice.setPitch(90);
                voice.speak(inputWordText);
                voice.deallocate();
            }
        });
    }

    // loai bo tat ca cac khoang trang thua
    public static String trimWord(String str) {
        return str.replaceAll("\\s\\s+", " ").trim();
    }

    public void searchWord(ActionEvent actionEvent) {
        String inputWordText = trimWord(inputWord.getText());
        
        // check tu gan dung neu inputWordText co van de
        inputWordText = DictionaryManagement.spelling(inputWordText);

        Dictionary wordList = DictionaryManagement.getDictionary();
        int dictionarySize = wordList.getWords().size();

        // tim vi tri cua inputWordText trong tu dien
        int indexWord = Dictionary.binarySearch(wordList, 0, dictionarySize - 1, inputWordText);

        if (indexWord == -1) {
            wordTarget.setText(inputWordText);
            wordExplain.setText("Xin lỗi, chúng tôi không thể tìm thấy từ này !!!");
        } else {
            Word element = wordList.getWords().get(indexWord);
            wordTarget.setText(element.getWord_target());
            wordExplain.setText(element.getWord_explain());
        }
    }

    public void searchUsingAPI(ActionEvent actionEvent) {
        try {
            //Public API:
            //https://api.tracau.vn/WBBcwnwQpV89/s/{vocabulary}/en
            String inputWordText = trimWord(inputWord.getText());
            inputWordText = ControllerEdit.formatWord(inputWordText);
            URL url = new URL("https://api.tracau.vn/WBBcwnwQpV89/s/" + inputWordText + "/en");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // day la status-code trong http protocol
            int responseCode = conn.getResponseCode();

            // 200 OK => Yeu cau dc xu ly thanh cong !!
            if (responseCode != 200 || responseCode == 404) {
                wordTarget.setText(inputWordText);
                wordExplain.setText("Xin lỗi, chúng tôi không thể tìm thấy nghĩa của câu này !!");
            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }

                // chuyen JSON -> Object
                JSONParser parser = new JSONParser();
                JSONObject dataObject = (JSONObject) parser.parse(String.valueOf(informationString));

                // tim den vi tri cua JSONObject dang luu tru en, vi
                JSONArray sentencesData = (JSONArray) dataObject.get("sentences");
                JSONObject fields = (JSONObject) sentencesData.get(1);
                JSONObject means = (JSONObject) fields.get("fields");

                wordTarget.setText(inputWordText);
                wordExplain.setText((String) means.get("vi"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showEdit(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("FXML/editUI.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Từ điển Anh - Việt - Chỉnh sửa từ điển");
        stage.setScene(scene);
        stage.show();
    }

    public void showHelp(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("FXML/helpUI.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Từ điển Anh - Việt - Hướng dẫn sử dụng");
        stage.setScene(scene);
        stage.show();
    }
}
