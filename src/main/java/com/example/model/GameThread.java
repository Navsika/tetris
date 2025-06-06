package com.example.model;

import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

public class GameThread extends Thread{
    private ActiveFigure activeFigure;
    private Background backGround;
    private Model model;
    private int score;
    private final PropertyChangeSupport pcs;
    private int normalDelay = 500;
    private int fastDelay = 50;
    private volatile int curDelay = normalDelay;

    public GameThread(Model model){
        this.model = model;
        pcs = new PropertyChangeSupport(this);
        backGround = new Background(Model.boardHeight, Model.boardWidth);
        this.score = 0;
        makeNewFigure();
    }

    public void makeNewFigure(){
        Figure figure = Figure.getRandomFigure();
        Coords position = new Coords(Model.boardWidth / 2 - 1, -figure.getHeight());
        activeFigure = new ActiveFigure(figure, position);
    }

    public void updateScore(int lines){
        Map<Integer, Integer> scores = new HashMap<>(Map.of(
                0, 0,
                1, 1,
                2, 5,
                3, 20,
                4, 100
        ));
        score += scores.getOrDefault(lines, 0);
        model.updateView();
    }

    public void setFastMode(boolean fast){
        curDelay = fast ? fastDelay : normalDelay;
    }

    public ActiveFigure getActiveFigure() {
        return activeFigure;
    }

    public Background getBackground() {
        return backGround;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void run() {
        while (model.isInGame()){
            if (activeFigure.moveDown(backGround.getField()))
                model.updateView();
            else {
                if (backGround.resetBackground(activeFigure)){
                    model.gameOver();
                    break;
                }
                int lines = backGround.cleanBackground();
                updateScore(lines);
                model.setFastDrop(false);
                makeNewFigure();
                if (!model.isInGame())
                    break;
                model.updateView();
            }

            try{
                Thread.sleep(curDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
