package main.java.com.example.model;

public class GameState {
    private final int[][] field;
    private final ActiveFigure figure;
    private final int score;

    public GameState(int[][] field, ActiveFigure figure, int score) {
        this.field = field;
        this.figure = figure;
        this.score = score;
    }

    public int[][] getField() {
        return field;
    }

    public ActiveFigure getFigure() {
        return figure;
    }

    public int getScore() {
        return score;
    }
}