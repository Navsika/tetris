package main.java.com.example;

import main.java.com.example.controller.Controller;
import main.java.com.example.model.Model;
import main.java.com.example.view.ConsoleView;
import main.java.com.example.view.Window;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);

        boolean consoleMode = args.length > 0 && args[0].equalsIgnoreCase("--console");

        if (consoleMode) {
            ConsoleView consoleView = new ConsoleView(model, controller);
            controller.setConsoleView(consoleView);
            consoleView.initialize();
        } else {
            Window window = new Window(model, controller);
            controller.setWindow(window);
        }
    }
}