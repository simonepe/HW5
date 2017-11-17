package Client.View;

import java.util.Scanner;
import Client.Controller.*;
import Client.Net.OutputHandler;

public class Interpreter implements Runnable{

    private final Scanner console = new Scanner(System.in);
    private boolean startGame = false;
    //private final SynchronizedStdOut outMgr = new SynchronizedStdOut();
    private Controller contr;

    public Interpreter(Controller contr){
        this.contr = contr;
    }
    public void start() {
        if (startGame) {
            return;
        }
        startGame = true;
        contr = new Controller();
        new Thread(this).start();
    }

        @Override
    public void run() {
        while (startGame) {
            try {
                String msg = new String(readNextLine());
                switch (msg.toUpperCase()) {
                    case "QUIT":
                        startGame = false;
                        contr.disconnect();
                        break;
                    case "CONNECT":
                        contr.connect("localhost",
                                      3333,
                                      new ConsoleOutput());
                        break;
                    case "YES":
                        contr.newGame();
                        break;
                    default:
                        contr.guess(msg);
                }
            } catch (Exception e) {
                System.out.println("Operation failed");
            }
        }
    }
    
    private String readNextLine() {
        return console.nextLine();
    }

    private class ConsoleOutput implements OutputHandler {
        @Override
        public void handleMsg(String msg) {
            System.out.println(msg);
        }
    }

}



