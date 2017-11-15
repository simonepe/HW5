package Server.Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class WordHandler {
/**
    String line;
    ArrayList<String> allWords;
    /**
     *
     * @author Simone
     

    public void WordHandler() {
        System.out.println("TIME TO READ WORDS");
        readWords();

    }

    public void readWords(){
        System.out.println("READ WORDS");
            


        try {
            FileInputStream fis = new FileInputStream("/Users/Simone/Downloads/words.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            try {
                while ((line = br.readLine()) != null) {
                    allWords.add(line);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("WORDS PROBLEM");
            }
        } catch(IOException ex){
            ex.printStackTrace();
            System.out.println("WORDS PROBLEM2");
        }

    }

    public String randomWord() throws Exception {
        System.out.println("RANDOM WORD");
        Random r = new Random();
        System.out.println(allWords.get(r.nextInt(allWords.size())));
        return allWords.get(r.nextInt(allWords.size()));
    }
    
    */
    public WordHandler(){
        
    }
    public String randomWord() throws Exception {
         String newWord = "";
         try{
             FileInputStream fis = new FileInputStream("/Users/Simone/Downloads/words.txt");
             BufferedReader br = new BufferedReader(new InputStreamReader(fis));
             
             String line;
             ArrayList<String> allWords = new ArrayList();
             
              try {
                while ((line = br.readLine()) != null) {
                    allWords.add(line);
                }
                
                Random r = new Random();
                int randomIndex = r.nextInt(allWords.size());
                newWord = allWords.get(randomIndex);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
         }catch(IOException ex){
         ex.printStackTrace();
     }
         return newWord.toUpperCase();
     }
   
}




