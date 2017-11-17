package Client.Controller;

import Client.Net.OutputHandler;
import Client.Net.ServerConnection;

import java.io.IOException;
import java.io.UncheckedIOException;


public class Controller {
    private final ServerConnection serverConnection = new ServerConnection();
    
    public void connect(String host, int port, OutputHandler outputHandler) {
        try { 
            serverConnection.connect(host, port, outputHandler);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }   
    }


    public void disconnect() throws IOException {
        serverConnection.disconnect();
    }

    public void guess(String guess) {
        serverConnection.makeGuess(guess);
    }


    public void newGame() {
        serverConnection.newGame();

    }

}
