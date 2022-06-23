package com.example.frenchtoenglishnotebook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    public Button wordsList2Btn;
    @FXML
    public Button deleteBtn;

    private ArrayList<Sentence> sentenceList1;
    private ArrayList<Sentence> sentenceList2;
    private ArrayList<Sentence> wordsList1 ;
    private ArrayList<Sentence> wordsList2 ;

    private ArrayList<Sentence> currentSentenceArrayList;

    private ObservableList<String> sentenceObservableList;
    private int cursor = 0;

    public HelloController() throws IOException, ClassNotFoundException {
         sentenceList1 = new ArrayList<>();
         sentenceList2 = new ArrayList<>();
         wordsList1 = new ArrayList<>();
         wordsList2 = new ArrayList<>();
         currentSentenceArrayList = sentenceList1;
        loadSavedList();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setSentenceObservableList();


        //listView.setItems(sentenceObservableList);



        if(currentSentenceArrayList.size() > 0)
           setFrench();
    }

    @FXML
    public void onSaveButtonClick(ActionEvent actionEvent) throws IOException {

        if(frenchTextField.getText().trim() != ""  && englishTextField.getText().trim() != "")
        {
            Sentence sentence = new Sentence(frenchTextField.getText(), englishTextField.getText());
            currentSentenceArrayList.add(sentence);
            sentenceObservableList.add(sentence.getFrench());

            saveList();
        }
        else
            errorMessage("Please complete \"french\" field and \"english\" field!");
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
        if(currentSentenceArrayList != sentenceList1) {
            currentSentenceArrayList = sentenceList1;
            refreshAll();
        }
    }
    @FXML
    public void onSentenceList2BtnClick(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if(currentSentenceArrayList != sentenceList2)
        {
            currentSentenceArrayList = sentenceList2;
            refreshAll();
        }
    }
    @FXML
    public void onWordsList1BtnClick(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if(currentSentenceArrayList != wordsList1)
        {
            currentSentenceArrayList = wordsList1;
            refreshAll();
        }
    }
    @FXML
    public void onWordsList2BtnClick(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if(currentSentenceArrayList != wordsList2)
        {
            currentSentenceArrayList = wordsList2;
            refreshAll();
        }
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
    private void refreshAll() throws IOException, ClassNotFoundException {
        frenchTextArea.setText("");
        englishTextArea.setText("");
        loadSavedList();
        setSentenceObservableList();
        listView.refresh();
    }

    private void errorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    private void setFrench(){
        frenchTextArea.setText(currentSentenceArrayList.get(cursor).getFrench());
        englishTextArea.setText("");
    }

    private void saveList() throws IOException {
        System.out.println("save1= " + (currentSentenceArrayList == sentenceList1));
        System.out.println("save2= " + (currentSentenceArrayList == wordsList2));
        FileOutputStream fos = new FileOutputStream(getCurrentListName());
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(currentSentenceArrayList);
        out.close();
        fos.close();
    }

    private void loadSavedList() throws IOException, ClassNotFoundException {
        System.out.println(getCurrentListName());
        FileInputStream fis = new FileInputStream(getCurrentListName());
        ObjectInputStream input = new ObjectInputStream(fis);
        currentSentenceArrayList = (ArrayList<Sentence>) input.readObject();


        if(currentSentenceArrayList == sentenceList1) sentenceList1 = cu
        else if(currentSentenceArrayList == sentenceList2)  sentenceList2 = (ArrayList<Sentence>) input.readObject();
        else if(currentSentenceArrayList == wordsList1) wordsList1 = (ArrayList<Sentence>) input.readObject() ;
        else
            wordsList2 = (ArrayList<Sentence>) input.readObject() ;

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


    //Renvois le nom du fichier sur lequel faire la sauvegarde ou le chargement
    private  String getCurrentListName()
    {//System.out.println("treturn= " + (currentSentenceArrayList == sentenceList1));
        if(currentSentenceArrayList == sentenceList1) return "sentenceList1.txt";
        else if(currentSentenceArrayList == sentenceList2) return "sentenceList2.txt";
        else if(currentSentenceArrayList == wordsList1) return "wordsList1.txt";
        return "wordsList2.txt";
    }


}