package com.example.contorller;

import com.example.model.Model;

import java.awt.event.KeyEvent;

public class Controller {
    private final Model model;
    public Controller(Model model) {
        this.model = model;
    }

    public void startGame(){
        model.startNewGame();
    }

    public void moveLeft(){
        model.moveFigureLeft();
    }

    public void moveRight(){
        model.moveFigureRight();
    }

    public void rotate(){
        model.rotateFigure();
    }

    public void drop(){
        model.setFastDrop(true);
        model.dropFigure();
    }

    public void quitGame(){
        model.gameOver();
    }

    public void showMenu(){
        model.openMenu();
    }

    public void gameOver(){
        model.gameOver();
    }

    public void showLeaderBoard(){
        model.openLeaderBoard();
    }

    public void showAbout(){
        model.openAbout();
    }

    public void updateLeaderBoard(String playerName){
        model.addPlayer(playerName);
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

}
