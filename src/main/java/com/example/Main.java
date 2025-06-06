package com.example;

import com.example.contorller.Controller;
import com.example.model.Model;
import com.example.view.ConsoleView;
import com.example.view.Window;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        boolean useConsole = false;

        for (String arg : args) {
            if (arg.equalsIgnoreCase("--console")) {
                useConsole = true;
                break;
            }
        }

        Model model = new Model();
        Controller controller = new Controller(model);

        if (useConsole) {
            ConsoleView consoleView = new ConsoleView(model, controller);
            consoleView.initialize();
        } else {
            SwingUtilities.invokeLater(() -> {
                Window window = new Window(model, controller);
                window.getRootPane().setFocusTraversalKeysEnabled(false);
                window.setVisible(true);
            });
        }
    }
}
