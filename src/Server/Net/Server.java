package Server.Net;

import Server.Controller.Controller;
import Server.Model.Scoreboard;
import Server.Model.WordHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by camillasvartsjo on 2017-11-09.
 */
public class Server {
    private final Controller contr = new Controller();
    public final WordHandler wordHandler = new WordHandler();
    private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private final List<ClientHandler> clients = new ArrayList<>();
    private int portNo = 3333;

    public static void main(String[] args) {
        Server server = new Server();
        server.parseArguments(args);
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
        clientSocket.setSoLinger(true, LINGER_TIME);
        clientSocket.setSoTimeout(TIMEOUT_HALF_HOUR);
        ClientHandler handler = new ClientHandler(this, clientSocket);
        synchronized (clients) {
            clients.add(handler);
        }
        Thread handlerThread = new Thread(handler);
        handlerThread.setPriority(Thread.MAX_PRIORITY);
        handlerThread.start();
    }

    private void parseArguments(String[] arguments) {
        if (arguments.length > 0) {
            try {
                portNo = Integer.parseInt(arguments[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number, using default.");
            }
        }
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
