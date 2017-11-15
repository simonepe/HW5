package Server.Model;

import Server.Controller.Controller;

/**
 * Created by camillasvartsjo on 2017-11-09.
 */
public class Scoreboard {
    
    public HangManGame currentGame;
    int score;
    String currentScore = "Your current score is: ";
    WordHandler wh;
    

    public Scoreboard(WordHandler wh) throws Exception {
        this.score = 0;
        this.wh = wh;
    }

        public String newGame(WordHandler wh, Controller contr) throws Exception {
           
        this.currentGame = new HangManGame(wh.randomWord(), contr);
            String newGameBegins = currentGame.start();
            return newGameBegins;
        }

    public String positiveScore() {
        score++;
       return currentScore+String.valueOf(score);
    }
    
    public String negativeScore() {
        score--;
        return currentScore+String.valueOf(score);
    }

}
