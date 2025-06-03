package main.java.com.example.controller;

import main.java.com.example.model.Model;
import main.java.com.example.view.ConsoleView;
import main.java.com.example.view.Window;

import java.awt.event.KeyEvent;

public class Controller {
    private final Model model;
    private Window window;
    private ConsoleView consoleView;

    public Controller(Model model) {
        this.model = model;
    }

    public void setWindow(Window window) {
        this.window = window;
        this.consoleView = null;
    }

    public void setConsoleView(ConsoleView consoleView) {
        this.consoleView = consoleView;
        this.window = null;
    }

    public void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> moveLeft();
            case KeyEvent.VK_RIGHT -> moveRight();
            case KeyEvent.VK_UP -> rotate();
            case KeyEvent.VK_DOWN -> drop();
            case KeyEvent.VK_Q -> quitGame();
        }
    }

    public void startGame() {
        model.startNewGame();
    }

    public void moveLeft() {
        model.moveFigureLeft();
    }

    public void moveRight() {
        model.moveFigureRight();
    }

    public void rotate() {
        model.rotateFigure();
    }

    public void drop() {
        model.dropFigure();
    }

    public void quitGame() {
        model.gameOver();
    }

    public void showMenu() {
        model.openMenu();
    }

    public void showLeaderboard() {
        model.openLeaderBoard();
    }

    public void showAbout() {
        model.openAbout();
    }

    public void updateLeaderBoard(String playerName) {
        model.addPlayer(playerName);
    }
}