package Server.Controller;


import Server.Model.HangManGame;
import Server.Model.Scoreboard;
import Server.Model.WordHandler;
import Server.Net.Server;
import Server.Net.ClientHandler;

/**
 * The server side controller. All calls to the server side model pass through here.
 */
public class Controller {
    private Scoreboard scoreboard;
    private Server server;
    private ClientHandler client;
    
    public Controller(){

    }

    public String newGame(Scoreboard scoreboard, WordHandler wh, Controller contr, 
                          ClientHandler client, Server server ) throws Exception {
        this.scoreboard = scoreboard;
        this.client = client;
        this.server = server;
        return scoreboard.newGame(wh, this);
    }


    public String guess(String guess, HangManGame currentGame) {
        return currentGame.guess(guess);
    }
    
    public void positiveScore(){
        String msg = scoreboard.positiveScore();
        server.newScore(client, msg);
    }
    
    public void negativeScore(){
        String msg = scoreboard.negativeScore();
        server.newScore(client, msg);
    }
}
