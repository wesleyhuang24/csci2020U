/*
Date: March, 07, 2017
Names: Edmond Jia Rong Liu(100588399), Wesley Huang(100547950), Arshdeep Benipal(100591622)
Purpose: Assignment 1 (Super mega ultra awesome spam annihilator 696969 )
cont. To train a spam detector and then test it on a data set that was provided and outputs
cont. a GUI that displays file name, actual class, spam probability, accuracy and precision
cont. using the Naive bayes spam filtering algorithm
*/
package sample;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class WordCounter {
        public TreeMap<String, Integer> numOfFiles;
        public TreeMap<String, Integer> wordCounts;
        public TreeMap<String, Double> wordProbability;
        public int spamSize;
        public int hamSize;

        //checks to see if its ham folder or spam folder and processes each file
        public WordCounter(File[] fileList, boolean isHam) throws IOException {
                wordCounts = new TreeMap<>();
                if(isHam){
                        hamSize = fileList.length;
                }
                else{
                        spamSize = fileList.length;
                }
                for(int i = 0; i < fileList.length; i++){
                        processFile(fileList[i]);
                }
                calcProbability(wordCounts, isHam);
        }

        //takes all the files and creates a tree map with all the unique words in it
        public void processFile(File file) throws IOException {
                if (file.isDirectory()) {
                        // process all of the files recursively
                        File[] filesInDir = file.listFiles();
                        for (int i = 0; i < filesInDir.length; i++) {
                                processFile(filesInDir[i]);
                        }
                }
                else if (file.exists()) {
                        // load all of the data, and process it into words
                        Scanner scanner = new Scanner(file);
                        numOfFiles = new TreeMap<>();
                        while (scanner.hasNext()) {
                                String word = scanner.next();
                                countWord(word);
                        }
                }
        }

        //increments the amount of times each unique word appears
        private void countWord(String word) {
                if (wordCounts.containsKey(word)) {
                        int oldCount = wordCounts.get(word);
                        wordCounts.put(word, oldCount + 1);
                }
                else {
                        wordCounts.put(word, 1);
                }
        }

        //calculates the probability of the word being spam
        public void calcProbability(TreeMap<String, Integer> wordCounts, boolean isHam) throws IOException{
                Set<String> keys = wordCounts.keySet();
                wordProbability = new TreeMap<>();
                Iterator<String> keyIterator = keys.iterator();

                while(keyIterator.hasNext()) {
                        String key = keyIterator.next();
                        int count = wordCounts.get(key);

                        if (isHam) {
                                wordProbability.put(key, (count/ (double)hamSize));
                        }
                        if(!isHam){
                                wordProbability.put(key, (count/ (double)spamSize));
                        }
                }
        }

}
