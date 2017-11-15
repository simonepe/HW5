package Client.StartUp;

import Client.Controller.Controller;
import Client.View.Interpreter;

public class Main {
    private static Controller contr;

    public static void main(String[] args){
        System.out.println("Type connect to connect to server");
        contr = new Controller();
        new Interpreter(contr).start();
    }
}
