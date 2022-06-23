package com.example.frenchtoenglishnotebook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    public TextField frenchTextField;
    @FXML
    public TextField englishTextField;
    @FXML
    public Button enter;
    @FXML
    public Button previous;
    @FXML
    public Button next;
    @FXML
    public Button translation;
    @FXML
    public ListView <String>listView;
    @FXML
    public TextArea frenchTextArea;
    @FXML
    public TextArea englishTextArea;

    private ArrayList<Sentence> sentenceArrayList = new ArrayList<>();

    private ObservableList<String> sentenceObservableList = FXCollections.observableArrayList();
    private int cursor = 0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.getItems().addAll(sentenceObservableList);

        if(sentenceArrayList.size() > 0)
           setFrench();
    }

    @FXML
    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {
        if(frenchTextField.getText().trim() != ""  && englishTextField.getText().trim() != "")
        {
            Sentence sentence = new Sentence(frenchTextField.getText(), englishTextField.getText());
            sentenceArrayList.add(sentence);
            sentenceObservableList.add(sentence.getFrench());
            listView.setItems(sentenceObservableList);
            saveList();
        }
        else
            errorMessage("Please complete \"french\" field and \"english\" field!");
    }

    @FXML
    public void onTranslationButtonClick(ActionEvent actionEvent) {

        if(sentenceArrayList.size() > 0)
        {
            if(sentenceArrayList.get(cursor).getFrench().trim() != "")
            {
                englishTextArea.setText(sentenceArrayList.get(cursor).getEnglish());
                if(frenchTextArea.getText() == "")
                    setFrench();
            }
            else
                    errorMessage("\"French\" field is empty!");
        }
        else
            errorMessage("No sentence to translate. The list is empty yet");

    }
    @FXML
    public void onNextButtonClick(ActionEvent actionEvent) {

        if(sentenceArrayList.size() < 0)
            errorMessage("No french sentence to dysplay! The list of sentence is empty");

        else if (sentenceArrayList.size() == 0 || (cursor + 1) == sentenceArrayList.size())
            errorMessage("There is no next sentence. You are at the last sentence in the list");
        else {
            cursor++;
            setFrench();
        }

    }
    @FXML
    public void onPreviousButtonClick(ActionEvent actionEvent) {
        if(sentenceArrayList.size() < 0)
            errorMessage("No french sentence to dysplay! The list of sentence is empty");
        else if (cursor < 1)
            errorMessage("There is no preceding sentence. You are at the beginning of the list.");
        else {
            cursor--;
            setFrench();
        }
    }

    @FXML
    public void onItemSelected(MouseEvent mouseEvent) {
        cursor = listView.getSelectionModel().getSelectedIndex();
        setFrench();
    }

    private void errorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    private void setFrench(){
        frenchTextArea.setText(sentenceArrayList.get(cursor).getFrench());
        englishTextArea.setText("");
    }

    private void saveList() throws IOException {
        FileOutputStream fos = new FileOutputStream("Sauvegarde.txt");
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(sentenceArrayList);
        out.close();
        fos.close();
    }



}