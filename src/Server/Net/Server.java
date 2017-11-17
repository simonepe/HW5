package Server.Net;

import Server.Controller.Controller;
import Server.Model.WordHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final Controller contr = new Controller();
    public final WordHandler wordHandler = new WordHandler();
    private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private final List<ClientHandler> clients = new ArrayList<>();
    private int portNo = 3333;

    public static void main(String[] args) {
        Server server = new Server();
        server.serve();
    }

    void removeHandler(ClientHandler handler) {
        synchronized (clients) {
            clients.remove(handler);
        }
    }

    private void serve() {
        try {
            ServerSocket listeningSocket = new ServerSocket(portNo);
            while (true) {
                Socket clientSocket = listeningSocket.accept();
                startHandler(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Server failure.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startHandler(Socket clientSocket) throws Exception {
        ClientHandler handler = new ClientHandler(this, clientSocket);
        synchronized (clients) {
            clients.add(handler);
        }
        Thread handlerThread = new Thread(handler);
        handlerThread.setPriority(Thread.MAX_PRIORITY);
        handlerThread.start();
    }

    public void newGame(ClientHandler client) throws Exception {
        String msg = contr.newGame(client.scoreboard, wordHandler, contr, client, this);
        client.sendMsg(msg);


    }
    public void guess(String guess, ClientHandler client) {
        String msg = contr.guess(guess, client.scoreboard.currentGame);
        client.sendMsg(msg);
        
    }

    public void newScore(ClientHandler client, String msg) {
        client.sendMsg(msg);
    }

}
