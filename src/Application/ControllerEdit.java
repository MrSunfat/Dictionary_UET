package Application;

import Dictionary.Dictionary;
import Dictionary.DictionaryManagement;
import Dictionary.Word;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class ControllerEdit implements Initializable {
    public Dictionary wordList = DictionaryManagement.getDictionary();
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label notify;
    @FXML
    private Label notify2;

    @FXML
    private TextField editWordTarget;
    @FXML
    private TextField editWordExplain;
    @FXML
    private TextField deleteWord;

    @FXML
    private TextField inputEditWord = new TextField();
    @FXML
    private TableView<Word> tableView = new TableView<Word>();
    @FXML
    private TableColumn<Word, String> targetColumn = new TableColumn<Word, String>();
    @FXML
    private TableColumn<Word, String> explainColumn = new TableColumn<Word, String>();

    private ObservableList<Word> wordObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wordObservableList = FXCollections.observableArrayList();
        int dictionarySize = DictionaryManagement.getDictionary().getWords().size();
        for (int i = 0; i < dictionarySize; i++) {
            Word element = DictionaryManagement.getDictionary().getWords().get(i);
            wordObservableList.add(element);
        }
        targetColumn.setCellValueFactory(new PropertyValueFactory<Word, String>("word_target"));
        explainColumn.setCellValueFactory(new PropertyValueFactory<Word, String>("word_explain"));
        tableView.setItems(wordObservableList);

        inputEditWord.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                wordObservableList.clear();
                for (int i = 0; i < dictionarySize; i++) {
                    Word element = DictionaryManagement.getDictionary().getWords().get(i);

                    // kiem tra newValue co ton tai trong element.getWord_target()
                    // va kiem tra element.getWord_target() co bat dau = newValue
                    if(element.getWord_target().toUpperCase().contains(ControllerMain.trimWord(newValue).toUpperCase())
                            && element.getWord_target().toUpperCase().indexOf(ControllerMain.trimWord(newValue).toUpperCase()) == 0) {
                        wordObservableList.add(element);
                    }
                }
                tableView.setItems(wordObservableList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {

            if (newValue != null) {
                String wordTarget = newValue.getWord_target();
                inputEditWord.setText(wordTarget);
                deleteWord.setText(wordTarget);
            }
        });
    }

    public static String formatWord(String word) {
        word = word.toLowerCase();
        String firstString = word.substring(0, 1).toUpperCase();
        String secondSting = word.substring(1, word.length());
        return (firstString + secondSting);
    }

    public void addWord(ActionEvent actionEvent) {
        String wordTarget = ControllerMain.trimWord(editWordTarget.getText());
        String wordExplain = ControllerMain.trimWord(editWordExplain.getText());

        if(wordTarget.equals("") || wordExplain.equals("")) {
            notify.setTextFill(Color.web("red"));
            notify.setText("Vui lòng, nhập đủ thông tin về từ này !!!");
            return;
        }

        // kiem tra xem tu duoc them , da ton tai trong tu dien chua
        int dictionarySize = wordList.getWords().size();

        // xem wordTarget da ton tai trong tu dien chua
        int indexWord = Dictionary.binarySearch(wordList, 0, dictionarySize - 1, wordTarget);

        if (indexWord != -1) {
            notify.setTextFill(Color.web("red"));
            notify.setText("Từ này đã tồn tại trong từ điển !!!");
        } else {
            // format lai tu vung, nghia => trong dep hon
            wordTarget = formatWord(wordTarget);
            wordExplain = wordExplain.toLowerCase();

            notify.setTextFill(Color.web("#24c175"));
            notify.setText("Thêm từ thành công !!!");

            // them tu vung moi vao tu dien
            wordList.getWords().add(new Word(wordTarget, wordExplain));

            Dictionary dictionary = DictionaryManagement.getDictionary();
            Dictionary.sortWord(dictionary, 0, dictionary.getWords().size() - 1);
            DictionaryManagement.dictionaryExportToFile();
            editWordTarget.setText("");
            editWordExplain.setText("");
        }
    }

    public void changeWord(ActionEvent actionEvent) {
        String wordTarget = ControllerMain.trimWord(editWordTarget.getText());
        String wordExplain = ControllerMain.trimWord(editWordExplain.getText());

        if(wordTarget.equals("") || wordExplain.equals("")) {
            notify.setTextFill(Color.web("red"));
            notify.setText("Vui lòng, nhập đủ thông tin về từ này !!!");
            return;
        }

        // tim vi tri cua tu vung can thay doi
        int dictionarySize = wordList.getWords().size();

        // tim vi tri cua wordTarget trong tu dien
        int indexChange = Dictionary.binarySearch(wordList, 0, dictionarySize - 1, wordTarget);

        if (indexChange == -1) {
            notify.setTextFill(Color.web("red"));
            notify.setText("Không thể sửa, vì từ này không tồn tại !!!");
        } else {
            // format lai tu vung, nghia => trong dep hon
            wordTarget = formatWord(wordTarget);
            wordExplain = wordExplain.toLowerCase();

            notify.setTextFill(Color.web("#24c175"));
            notify.setText("Thay đổi từ thành công !!!");

            // them tu vung moi vao tu dien
            DictionaryManagement.getDictionary().getWords().set(indexChange ,new Word(wordTarget, wordExplain));
            DictionaryManagement.dictionaryExportToFile();
            editWordTarget.setText("");
            editWordExplain.setText("");
        }
    }

    public void deleteHandleWord(ActionEvent actionEvent) {
        String wordTarget = ControllerMain.trimWord(deleteWord.getText());

        if(wordTarget.equals("")) {
            notify2.setTextFill(Color.web("red"));
            notify2.setText("Vui lòng, nhập đủ thông tin về từ cần xóa !!!");
            return;
        }

        // tim vi tri cua tu vung can xoa
        int dictionarySize = wordList.getWords().size();

        // tim vi tri cua wordTarget trong tu dien
        int indexChange = Dictionary.binarySearch(wordList, 0, dictionarySize - 1, wordTarget);

        if (indexChange == -1) {
            notify2.setTextFill(Color.web("red"));
            notify2.setText("Không thể xóa, vì từ này không tồn tại !!!");
        } else {
            notify2.setTextFill(Color.web("#24c175"));
            notify2.setText("Xóa từ thành công !!!");

            // them tu vung moi vao tu dien
            wordList.getWords().remove(indexChange);
            DictionaryManagement.dictionaryExportToFile();
            editWordTarget.setText("");
            editWordExplain.setText("");
        }
    }

    public void returnMain(ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("FXML/mainUI.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("CSS/styleUI.css").toExternalForm());
        stage.setTitle("Từ điển Anh - Việt");
        stage.setScene(scene);
        stage.show();
    }
}
