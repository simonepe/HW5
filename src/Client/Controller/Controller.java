package Client.Controller;

import Client.Net.OutputHandler;
import Client.Net.ServerConnection;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;


public class Controller {
    private final ServerConnection serverConnection = new ServerConnection();
    
    public void connect(String host, int port, OutputHandler outputHandler) {

        CompletableFuture.runAsync(() -> {
            try { 
                serverConnection.connect(host, port, outputHandler);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        }).thenRun(() -> outputHandler.handleMsg("Connected to host: " + host + " and port: " + port));   
    }


    public void disconnect() throws IOException {
        serverConnection.disconnect();
    }

    public void guess(String guess) {
        CompletableFuture.runAsync(() -> serverConnection.makeGuess(guess));
    }


    public void newGame() {
        serverConnection.newGame();

    }

}
