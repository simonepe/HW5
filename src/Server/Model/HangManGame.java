package Server.Model;

import Server.Controller.Controller;

public class HangManGame {

    int remainingGuesses;
    boolean correctGuess;
    String[] wordLetters;
    String[] output;
    String word;
    String[] guessedChars;
    String won = "YOU WON!";
    String lost = "YOU LOST!";
    String nextGuess = "Enter your next guess";
    String gameBegins = "A new game has started, make your first guess.";
    String startOver = "type YES to start a new game";
    String youGuessed = "You guessed: ";
    String guessesRemaining = "Guesses remaining: ";
    String searchedWord = "The word we were looking for was: ";
    Controller contr;


    public HangManGame(String word, Controller contr){
        this.word = word;
        this.contr = contr;
        remainingGuesses = word.length();
        wordLetters = word.split("");
        start();

      
    }

    public String start(){
        
        guessedChars = new String[word.length()];
        output = new String[word.length()*2];
        int i = 0;
        while (i < word.length()*2){
            output[i] = "_ ";
            i++;
        }
        int t = 0;
        StringBuffer sb = new StringBuffer("");
        for( String si : wordLetters){
            sb.append(output[t]);
            t++;
        }
        return gameBegins + System.lineSeparator() + sb.toString();
    }
    public String guess(String guess){
        boolean allLettersDone = true;
        boolean correctGuess = false;
        if (guess.length() > 1){
            if (guess.equals(word)) {
                                       
                return won();

            }
            else {
                                        
                return lost();
            }
        }
        int i = 0;
        for (String s : wordLetters){
            if (guess.equals(s)){         
                output[i] = guess;
                correctGuess = true;
            }
            else if (output[i*2].equals("_ ")){
                allLettersDone = false;
               
            }
            i++;
        }
        if (allLettersDone) {
            return won();          
        }
       
         int t = 0;
                StringBuffer sb = new StringBuffer("");
                for( String si : wordLetters){
                    sb.append(output[t]);
                    t++;
                }
                if(sb.toString().equals(word)){
                   return won();
                }
                if (correctGuess == false){
                    remainingGuesses--;
                    if(remainingGuesses < 1){
                        return lost();
                    }
                }
                return youGuessed + guess + System.lineSeparator() 
                        + nextGuess + System.lineSeparator() 
                        + guessesRemaining + remainingGuesses + System.lineSeparator()
                        + sb.toString();
    }
    private String won(){
        contr.positiveScore();  
        return won + System.lineSeparator() + startOver;
    }
    
    private String lost(){
        contr.negativeScore();
        return lost + System.lineSeparator()
                + searchedWord + word + System.lineSeparator()
                + startOver;
    }
}
