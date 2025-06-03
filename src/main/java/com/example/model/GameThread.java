package main.java.com.example.model;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

public class GameThread extends Thread {
    private ActiveFigure activeFigure;
    private Background background;
    private Model model;
    private int score;
    private volatile boolean dropping;
    private final PropertyChangeSupport pcs;

    public GameThread(Model model) {
        this.model = model;
        this.background = new Background(Model.boardWidth, Model.boardHeight);
        this.score = 0;
        this.dropping = false;
        this.pcs = new PropertyChangeSupport(this);
        makeNewFigure();
    }

    public void makeNewFigure(){
        Figure figure = Figure.getRandomFigure();
        Coords point = new Coords(Model.boardWidth / 2 - 1, -figure.getHeight());
        activeFigure = new ActiveFigure(figure, point);
    }

    @Override
    public void run() {
        while (model.isInGame()) {
            if (dropping)
                continue;

            if (activeFigure.moveDown(background.getField())) {
                model.updateView();
            } else {
                if (background.resetBackground(activeFigure)) {
                    model.gameOver();
                    break;
                }
                int linesNum = background.cleanBackground(activeFigure);
                updateScore(linesNum);
                makeNewFigure();
                if (!model.isInGame()) break;
                model.updateView();
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void updateScore(int linesNum) {
        Map<Integer, Integer> scores = new HashMap<>(Map.of(
                0, 0,
                1, 1,
                2, 5,
                3, 20,
                4, 100
        ));
        score += scores.get(linesNum);
        pcs.firePropertyChange("score", null, score);
    }

    public ActiveFigure getActiveFigure() {
        return activeFigure;
    }

    public Background getBackground() {
        return background;
    }

    public int getScore() {
        return score;
    }
}