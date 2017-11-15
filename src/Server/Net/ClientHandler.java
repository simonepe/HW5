/*
 * The MIT License
 *
 * Copyright 2017 Leif Lindbäck <leifl@kth.se>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package Server.Net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.StringJoiner;

import Common.Command;
import Common.MessageException;
import Server.Model.*;

import static Common.Command.DISCONNECT;
import static Common.Command.NEWGAME;
import static Common.Command.NO_COMMAND;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles all communication with one particular chat client.
 */
public class ClientHandler implements Runnable {

    private final Server server;
    private final Socket clientSocket;
    private BufferedReader fromClient;
    private PrintWriter toClient;
    private boolean connected;
    public Scoreboard scoreboard;

    /**
     * Creates a new instance, which will handle communication with one specific client connected to
     * the specified socket.
     *
     * @param clientSocket The socket to which this handler's client is connected.
     */
    ClientHandler(Server server, Socket clientSocket) throws Exception {
        this.server = server;
        this.clientSocket = clientSocket;
        connected = true;
        scoreboard= new Scoreboard(server.wordHandler);
    }

    /**
     * The run loop handling all communication with the connected client.
     */
    @Override
    public void run() {
        try {
            boolean autoFlush = true;
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            toClient = new PrintWriter(clientSocket.getOutputStream(), autoFlush);
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }

        while (connected) {
            try {
                String s = fromClient.readLine();
                
                Message msg = new Message(s);
                switch (msg.msgType) {

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
                        throw new MessageException("Received corrupt message: " + msg.receivedString);
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

    private static class Message {
        private Command msgType;
        private String receivedString;

        private Message(String receivedString) {
            parse(receivedString);
            this.receivedString = receivedString;
        }

        private void parse(String strToParse) {
            try {
                String up = strToParse.toUpperCase();
                if(null != up)switch (up) {
                    case "NEWGAME":
                        msgType = Command.valueOf(up);
                        break;
                    case "DISCONNECT":
                        msgType = Command.valueOf(up);
                        break;
                    default:
                        msgType = Command.NO_COMMAND;
                        break;
                }

            } catch (Throwable throwable) {
                throw new MessageException(throwable);
            }
        }

    }

}
