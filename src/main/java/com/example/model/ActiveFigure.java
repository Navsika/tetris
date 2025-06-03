package main.java.com.example.model;

import java.util.Arrays;

public class ActiveFigure {
    private Figure figure;
    private Coords position; // top left corner

    public ActiveFigure(Figure figure, Coords at) {
        this.figure = figure;
        this.position = at;
    }

    public Coords getPosition() {
        return position;
    }

    public boolean canMoveTo(int newX, int newY, int[][] background) {
        int figureWidth = figure.getWidth();
        int figureHeight = figure.getHeight();
        int[][] shape = figure.getShape();

        for (int x = 0; x < figureWidth; x++)
            for (int y = 0; y < figureHeight; y++)
                if (shape[y][x] == 1) {
                    int boardX = newX + x;
                    int boardY = newY + y;
                    System.out.println("Checking boardY=" + boardY + " vs height=" + Model.boardHeight);

                    if (boardX < 0 || boardX >= Model.boardWidth ||
                            boardY >= Model.boardHeight) {//if lower than lb
                        return false;
                    }
                    if (boardY < 0)
                        continue;
                    if (background[boardY][boardX] != 0)
                        return false;
                }

        return true;
    }

    public boolean moveLeft(int[][] background) {
        int newX = position.x() - 1;
        int newY = position.y();

        if (canMoveTo(newX, newY, background)) {
            position = new Coords(newX, newY);
            return true;
        }
        return false;
    }

    public boolean moveRight(int[][] background) {
        int newX = position.x() + 1;
        int newY = position.y();

        if (canMoveTo(newX, newY, background)) {
            position = new Coords(newX, newY);
            return true;
        }
        return false;
    }

    public boolean moveDown(int[][] background) {
        int newX = position.x();
        int newY = position.y() + 1;
        System.out.println("Trying to move down to: x=" + position.x() + ", y=" + (position.y() + 1));

        if (canMoveTo(newX, newY, background)) {
            position = new Coords(newX, newY);
            return true;
        }
        return false;
    }

    public void dropToBottom(int[][] background) {
        while (moveDown(background)) {
            // krep moving down until it cant
        }
    }

    public void turnRight(int[][] background) {
        Figure turnFigure = figure.turnRight();
        Figure saveFigure = figure;
        figure = turnFigure;
        if (!canMoveTo(position.x(), position.y(), background)) {
            figure = saveFigure;
        }
    }

    public int getColorCode() {
        return figure.getColorCode();
    }

    public int[][] getShape() {
        return figure.getShape();
    }

    public int getHeight() {
        return figure.getHeight();
    }

    public int getWidth() {
        return figure.getWidth();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("position=").append(position).append("\nshape:\n");
        int[][] shape = figure.getShape();
        for (int[] row : shape) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }
}