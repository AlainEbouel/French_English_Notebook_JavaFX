package com.example.frenchtoenglishnotebook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.*;
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
    @FXML
    public Button sentenceList1Btn;
    @FXML
    public Button sentenceList2Btn;
    @FXML
    public Button wordsList1Btn;
    @FXML
    public Button pronunciationBtn;
    @FXML
    public Button deleteBtn;

    private ArrayList<Sentence> sentenceList1;
    private ArrayList<Sentence> sentenceList2;
    private ArrayList<Sentence> wordsList1 ;
    private ArrayList<Sentence> wordsList2 ;
    private ArrayList<Sentence> currentSentenceArrayList;
    private ObservableList<String> sentenceObservableList;
    private int cursor = 0;
    String currentListName ;
    private ArrayList<Button> buttonArrayList =  new ArrayList<>();
    private ArrayList<Button> buttonArrayList2 =  new ArrayList<>();

    public HelloController() throws IOException, ClassNotFoundException {
        // sentenceList1 = new ArrayList<>();
        // sentenceList2 = new ArrayList<>();
        // wordsList1 = new ArrayList<>();
        // wordsList2 = new ArrayList<>();
        currentListName = "sentenceList1.txt";
        currentSentenceArrayList = new ArrayList<>();
        loadSavedList();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setSentenceObservableList();

        buttonArrayList.add(sentenceList1Btn);
        buttonArrayList.add(sentenceList2Btn);
        buttonArrayList.add(wordsList1Btn);
        buttonArrayList.add(pronunciationBtn);
        buttonArrayList2.add(enter);
        buttonArrayList2.add(translation);
        buttonArrayList2.add(previous);
        buttonArrayList2.add(next);
        buttonArrayList2.add(deleteBtn);

       // hoverBtn();

        if(currentSentenceArrayList.size() > 0)
           setFrench();
        setButtonColor(sentenceList1Btn);
    }

    @FXML
    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {

     CreateNewSentenceAndExpression();
    }
    @FXML
    public void onTranslationButtonClick(ActionEvent actionEvent) {

        if(currentSentenceArrayList.size() > 0)
        {
            if(currentSentenceArrayList.get(cursor).getFrench().trim() != "")
            {
                englishTextArea.setText(currentSentenceArrayList.get(cursor).getEnglish());
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

        if(currentSentenceArrayList.size() < 0)
            errorMessage("No french sentence to dysplay! The list of sentence is empty");

        else if (currentSentenceArrayList.size() == 0 || (cursor + 1) == currentSentenceArrayList.size())
            errorMessage("There is no next sentence. You are at the last sentence in the list");
        else {
            cursor++;
            setFrench();
        }

    }
    @FXML
    public void onPreviousButtonClick(ActionEvent actionEvent) {
        if(currentSentenceArrayList.size() < 0)
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
    @FXML
    public void onSentenceList1BtnClick(ActionEvent actionEvent) throws IOException, ClassNotFoundException {

        currentListName = "sentenceList1.txt";
        refreshAll(sentenceList1Btn);
    }
    @FXML
    public void onSentenceList2BtnClick(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        currentListName = "sentenceList2.txt";
            refreshAll(sentenceList2Btn);

    }
    @FXML
    public void onWordsList1BtnClick(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        currentListName = "wordsList1.txt";
            refreshAll(wordsList1Btn);
    }
    @FXML
    public void onPronunciationBtnClick(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        currentListName = "wordsList2.txt";
            refreshAll(pronunciationBtn);
    }

    @FXML
    public void onDeleteBtnCLick(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you want to permanently delete this item?");
        alert.setHeaderText("");
        String choice = alert.showAndWait().get().getButtonData().toString();
        int index;
        if(choice.equalsIgnoreCase("OK_DONE")) {
            index = listView.getSelectionModel().getSelectedIndex();
            currentSentenceArrayList.remove(index);
            listView.getItems().remove(index);
            saveList();
        }
    }

    private void hoverBtn(){
        for (Button btn : buttonArrayList) {
            btn.setOnMouseEntered(event -> btn.setStyle("-fx-background-radius: 30"));
            btn.setStyle("-fx-background-color :  rgb(84, 108, 173)");
        }
        for (Button btn : buttonArrayList2) {
            btn.setOnMouseExited(event -> btn.setStyle("-fx-background-radius: 10"));
            btn.setStyle("-fx-background-color :  rgb(84, 108, 173)");
        }

    }



    public void onEnglishEnterKeyReleased(KeyEvent keyEvent) throws IOException {
        if(keyEvent.getCode() == KeyCode.ENTER)
            CreateNewSentenceAndExpression();
    }
    public void onFrenchEnterKeyReleased(KeyEvent keyEvent) throws IOException {
        if(keyEvent.getCode() == KeyCode.ENTER)
            CreateNewSentenceAndExpression();
    }


    //Création des nouvelles phrase et expressions
    private void CreateNewSentenceAndExpression() throws IOException {
        if(frenchTextField.getText().trim() != ""  && englishTextField.getText().trim() != "")
        {
            Sentence sentence = new Sentence(frenchTextField.getText(), englishTextField.getText());
            currentSentenceArrayList.add(sentence);
            sentenceObservableList.add(sentence.getFrench());
            listView.getItems().setAll(sentenceObservableList);
            listView.refresh();
            saveList();
            frenchTextField.setText("");
            englishTextField.setText("");
        }
        else
            errorMessage("Please complete \"french\" field and \"english\" field!");
    }
    private void refreshAll(Button button) throws IOException, ClassNotFoundException {
        frenchTextArea.setText("");
        englishTextArea.setText("");
        loadSavedList();
        setSentenceObservableList();
        listView.refresh();
        setButtonColor(button);
        cursor = 0;
        frenchTextArea.setText(sentenceObservableList.get(cursor));
    }

    private void errorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    private void setFrench(){
        frenchTextArea.setText(currentSentenceArrayList.get(cursor).getFrench());
        englishTextArea.setText("");
        listView.getSelectionModel().select(cursor);
    }

    private void saveList() throws IOException {

        FileOutputStream fos = new FileOutputStream(currentListName);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(currentSentenceArrayList);
        out.close();
        fos.close();
    }

    private void loadSavedList() throws IOException, ClassNotFoundException {

        FileInputStream fis = new FileInputStream(currentListName);
        ObjectInputStream input = new ObjectInputStream(fis);
        currentSentenceArrayList = (ArrayList<Sentence>) input.readObject();

    }
    private void setSentenceObservableList()
    {
        sentenceObservableList =  FXCollections.observableArrayList();
        listView.getItems().clear();;
        for(Sentence sentence : currentSentenceArrayList)
            sentenceObservableList.add(sentence.getFrench());
        listView.getItems().addAll(sentenceObservableList);
        listView.getSelectionModel().select(0);

    }

   private void setButtonColor(Button button)
   {
       for(Button btn : buttonArrayList)
       {
           if(btn != button) {
               btn.setStyle("-fx-opacity : 0.5 ");
           }
       }
       button.setStyle("-fx-opacity : 1");
       button.setStyle("-fx-background-color :  rgb(84, 108, 173)");
   }



}