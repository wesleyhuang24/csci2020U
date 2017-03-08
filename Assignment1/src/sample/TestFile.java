/*
Date: March, 07, 2017
Names: Edmond Jia Rong Liu(100588399), Wesley Huang(100547950), Arshdeep Benipal(100591622)
Purpose: Assignment 1 (Super mega ultra awesome spam annihilator 696969 )
cont. To train a spam detector and then test it on a data set that was provided and outputs
cont. a GUI that displays file name, actual class, spam probability, accuracy and precision
cont. using the Naive bayes spam filtering algorithm
*/
package sample;

import java.text.DecimalFormat;


public class TestFile {
    private String filename;
    private double spamProbability;
    private String guess;
    private String actualClass;
    private int correct;
    private String roundedSpamProbability;

    //to help display file name, actual class and spam probability
    public TestFile(String filename,double spamProbability,String guess, String actualClass) {
        if(filename.indexOf("0") != -1 ) {
            this.filename = filename.substring(filename.indexOf("0"));
        }
        else{
            this.filename = filename.substring(filename.indexOf("cmds"));
        }
            this.spamProbability = spamProbability;
            this.guess = guess;
            this.actualClass = actualClass;
            if (actualClass.equalsIgnoreCase(guess)) {
                this.correct = 1;
            } else {
                this.correct = 0;
            }
            this.roundedSpamProbability = this.getSpamProbRounded();

    }


    public String getFilename() {
        return this.filename;
    }
    public double getSpamProbability() {
        return this.spamProbability;
    }
    //rounds the spam probability to 5 decimal places
    public String getSpamProbRounded() {
        DecimalFormat df = new DecimalFormat("0.00000");
        return df.format(this.spamProbability);
    }

    public String getActualClass() {
        return this.actualClass;
    }
    public String getGuess(){
        return this.guess;
    }
    public int getCorrect(){
        return this.correct;
    }
    public String getRoundedSpamProbability(){
        return this.roundedSpamProbability;
    }


}
