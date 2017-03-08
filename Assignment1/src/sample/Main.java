/*
Date: March, 07, 2017
Names: Edmond Jia Rong Liu(100588399), Wesley Huang(100547950), Arshdeep Benipal(100591622)
Purpose: Assignment 1 (Super mega ultra awesome spam annihilator 696969 )
cont. To train a spam detector and then test it on a data set that was provided and outputs
cont. a GUI that displays file name, actual class, spam probability, accuracy and precision
cont. using the Naive bayes spam filtering algorithm
*/
package sample;


import javafx.application.Application;
import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.*;
import java.io.*;
import java.util.*;

public class Main extends Application {
    Stage window;
    TableView<TestFile> table;
    public ObservableList<TestFile> testData = FXCollections.observableArrayList();
    public TreeMap<String, Double> spamWord;

    public static void main(String[] args) {
        //Executes the program
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        //This is the directory selector. It opens the prompt for the user to pick a folder
        DirectoryChooser chooser = new DirectoryChooser();
        //If nothing is selected, the default directory is C drive for obvious reasons
        File defaultDirectory = new File("C://");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(primaryStage);
        //Searches for ham,ham2,spam in the test and train folders
        File dirHam1 = new File(selectedDirectory + "/train/ham");
        File dirHam2 = new File(selectedDirectory + "/train/ham2");
        File dirSpam = new File(selectedDirectory + "/train/spam");
        File dirTestHam = new File(selectedDirectory + "/test/ham");
        File dirTestSpam = new File(selectedDirectory + "/test/spam");

        //takes the stream from the directories and stores them in an array of type File
        File[] ham1 = dirHam1.listFiles();
        File[] ham2 = dirHam2.listFiles();
        File[] ham12 = new File[ham1.length + ham2.length];
        File[] spam = dirSpam.listFiles();
        File[] testHam = dirTestHam.listFiles();
        File[] testSpam = dirTestSpam.listFiles();
        //combines the 2 ham folders(array) into a single folder(array)
        for(int i = 0; i< ham1.length;i++){
            ham12[i] = ham1[i];
        }
        for(int i = ham1.length; i < ham12.length; i++){
            ham12[i] = ham2[i-ham1.length];
        }

        //W|H && W|S
        WordCounter hamFrequency = new WordCounter(ham12, true);
        WordCounter spamFrequency = new WordCounter(spam, false);

        calcs(hamFrequency, spamFrequency);

        calcProbabilityTotal(testHam, spamWord);
        calcProbabilityTotal(testSpam, spamWord);

        double correct = 0.0;
        double incorrect = 0.0;
        for(int i = 0; i <testData.size(); i++){
            correct += testData.get(i).getCorrect();
            if(testData.get(i).getCorrect() == 0){
                incorrect += 1;
            }
        }
        double accuracy = correct/testData.size();

        correct = 0.0;
        incorrect = 0.0;
        for(int i =0 ; i < testData.size(); i++){
            if(testData.get(i).getActualClass() == "Spam"){
                if(testData.get(i).getCorrect() == 1){
                    correct += 1;
                }
                else {
                    incorrect += 1;
                }
            }
        }
        double precision = correct/(incorrect+correct);

        //GUI

        window = primaryStage;
        window.setTitle("String text(Can't believe its not not spam detector over 9000!!!!!!!!! version.69.AlphaEpsilonDelta).toUppercase(); ");

        //File Name Column
        TableColumn<TestFile, String> fileColumn = new TableColumn<>("File");
        fileColumn.setMinWidth(400);
        fileColumn.setCellValueFactory(new PropertyValueFactory<>("filename"));

        //Actual Class Column
        TableColumn<TestFile, String> aClassColumn = new TableColumn<>("Actual Class");
        aClassColumn.setMinWidth(100);
        aClassColumn.setCellValueFactory(new PropertyValueFactory<>("actualClass"));

        //Spam Probability Column
        TableColumn<TestFile, String> spamProbColumn = new TableColumn<>("Spam Probability");
        spamProbColumn.setMinWidth(400);
        spamProbColumn.setCellValueFactory(new PropertyValueFactory<>("roundedSpamProbability"));

        //Accuracy and Precision labels
        Label labelPercision = new Label("Percision:" + precision);
        Label labelAccuracy = new Label("Accuracy:" + accuracy);

        //Creates a table and adds columns into table
        table = new TableView<>();
        table.setItems(testData);
        table.getColumns().addAll(fileColumn, aClassColumn,spamProbColumn);

        //Creates a VBox and adds table to it and labels
        VBox vBox = new VBox();
        vBox.getChildren().addAll(table,labelAccuracy,labelPercision);

        //Creates an output screen and displays VBox
        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();




    }

    //Uses a treemap to calculate S|W
    public void calcs(WordCounter hamFrequency, WordCounter spamFrequency){
        double probability = 0.0;
        spamWord = new TreeMap<>();
        Iterator itr = spamFrequency.wordProbability.entrySet().iterator();
        while (itr.hasNext()){
            Map.Entry sp = (Map.Entry)itr.next();
            if(hamFrequency.wordCounts.containsKey(sp.getKey())){
                probability = ((Double) sp.getValue())/(((Double)sp.getValue())+hamFrequency.wordProbability.get(sp.getKey()));
                spamWord.put(sp.getKey().toString(),probability);
            }
            else {
                spamWord.put(sp.getKey().toString(), 1.0);
            }
            itr.remove();
        }
    }

    //Calculates Prob for testing Pr(S|F)
    private void calcProbabilityTotal(File[] fileList, TreeMap<String, Double> map) throws IOException {
        for (File file : fileList) {
            String loc = file.getPath();
            String actualClass = "Ham";
            if (loc.contains("spam")) {
                actualClass = "Spam";
            }
            double total = 0;
            FileReader fileRead = new FileReader(file);
            Scanner scan = new Scanner(fileRead);
            while (scan.hasNext()) {
                String text = scan.next().toLowerCase();
                if (map.containsKey(text)) {
                    double wSpamProb = map.get(text);
                    if (wSpamProb > 0.0f && wSpamProb < 1.0f) {
                        total = total + Math.log(1 - wSpamProb) - Math.log(wSpamProb);
                    }
                }
            }
            //Determines if Actual class is spam or ham
            fileRead.close();
            String guess = actualClass;
            double spamProb = 1 / (1 + Math.pow(Math.E, total));
            if (spamProb < 0.5) {
                guess = "Ham";
            }
            else{
                guess = "Spam";
            }
            TestFile testF = new TestFile(loc, spamProb, guess, actualClass);
            testData.add(testF);
        }
    }
}
