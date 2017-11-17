package Client.Net;


import Common.Command;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnection {
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private static final int TIMEOUT_HALF_MINUTE = 30000;
    private Socket socket;
    private PrintWriter toServer;
    private Scanner fromServer;
    private volatile boolean connected;

    public void connect(String host, int port, OutputHandler broadcastHandler) throws
            IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), TIMEOUT_HALF_MINUTE);
        socket.setSoTimeout(TIMEOUT_HALF_HOUR);
        connected = true;
        boolean autoFlush = true;
        toServer = new PrintWriter(socket.getOutputStream(), autoFlush);
        fromServer = new Scanner(new InputStreamReader(socket.getInputStream()));
        new Thread(new Listener(broadcastHandler)).start();
        newGame();
    }


    public void disconnect() throws IOException {
        toServer.println(Command.DISCONNECT);
        toServer.flush();
        socket.close();
        socket = null;
        connected = false;
    }


    public void newGame() {
        toServer.println(Common.Command.NEWGAME.toString());
    }


    public void makeGuess(String guess) {
        toServer.println(guess);
    }


    private class Listener implements Runnable {
        private final OutputHandler outputHandler;

        private Listener(OutputHandler outputHandler) {
            this.outputHandler = outputHandler;
        }

        @Override
        public void run() {
            try {
                for (;;) {
                    outputHandler.handleMsg(fromServer.nextLine());
                }
            } catch (Throwable connectionFailure) {
                if (connected) {
                    outputHandler.handleMsg("Lost connection.");
                }
            }
        }

    }
}
