package Server.Net;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.Socket;

import Common.Command;
import Common.MessageException;
import Server.Model.*;

import static Common.Command.DISCONNECT;
import static Common.Command.NEWGAME;
import static Common.Command.NO_COMMAND;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private final Server server;
    private final Socket clientSocket;
    private Scanner fromClient;
    private PrintWriter toClient;
    private boolean connected;
    public Scoreboard scoreboard;

    ClientHandler(Server server, Socket clientSocket) throws Exception {
        this.server = server;
        this.clientSocket = clientSocket;
        connected = true;
        scoreboard= new Scoreboard(server.wordHandler);
    }

    @Override
    public void run() {
        try {
            boolean autoFlush = true;
            fromClient = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
            toClient = new PrintWriter(clientSocket.getOutputStream(), autoFlush);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }

        while (connected) {
            try {
                String s = fromClient.nextLine();
                System.out.println("SERVER GOT: " + s);
                
                inputType msg = new inputType(s);
                switch (msg.commandType) {

                    case NEWGAME:
                        server.newGame(this);
                        break;
                    case DISCONNECT:
                        disconnectClient();
                        break;
                    case NO_COMMAND:
                        server.guess(msg.receivedString.toUpperCase(), this);
                        break;
                    default:
                        throw new MessageException("Corrupt message: " + msg.receivedString);
                }
            } catch (IOException ioe) {
                disconnectClient();
                throw new MessageException(ioe);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void sendMsg(String msg) {
        System.out.println("MSG SENT: " + msg);
        toClient.println(msg);
    }

    private void disconnectClient() {
        try {
            clientSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        connected = false;
        server.removeHandler(this);
    }

    private static class inputType {
        private Command commandType;
        private String receivedString;

        private inputType(String receivedString) {
            parse(receivedString);
            this.receivedString = receivedString;
        }

        private void parse(String strToParse) {
            try {
                String up = strToParse.toUpperCase();
                if(null != up)switch (up) {
                    case "NEWGAME":
                        commandType = Command.valueOf(up);
                        break;
                    case "DISCONNECT":
                        commandType = Command.valueOf(up);
                        break;
                    default:
                        commandType = Command.NO_COMMAND;
                        break;
                }

            } catch (Throwable throwable) {
                throw new MessageException(throwable);
            }
        }

    }

}
