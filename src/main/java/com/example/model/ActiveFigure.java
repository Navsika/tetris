package com.example.model;

public class ActiveFigure {
    private Figure figure;
    private Coords position;
    //top left corner of figure

    public ActiveFigure(Figure figure, Coords position){
        this.figure = figure;
        this.position = position;
    }

    public Coords getPosition(){
        return position;
    }

    public boolean canMoveTo(int newX, int newY, int[][] background){
        int figureWidth = figure.getWidth();
        int figureHeight = figure.getHeight();
        int[][] shape = figure.getShape();

        for (int y = 0; y < figureHeight; y++)
            for (int x = 0; x < figureWidth; x++){
                if (shape[y][x] == 1){
                    int boardX = newX + x;
                    int boardY = newY + y;

                    if (boardX < 0 || boardX >= Model.boardWidth || boardY >= Model.boardHeight)
                        return false;
                    if (boardY >= 0 && background[boardY][boardX] != 0)
                        return false;
                }
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

    public boolean moveDown(int[][] background){
        int newX = position.x();
        int newY = position.y() + 1;
        if (canMoveTo(newX, newY, background)){
            position = new Coords(newX, newY);
            return true;
        }
        return false;
    }

    public boolean turnRight(int[][] background) {
        Figure newFigure = figure.turnRight();
        Figure saved = figure;
        figure = newFigure;
        if (!canMoveTo(position.x(), position.y(), background)) {
            figure = saved;
            return false;
        }
        return true;
    }

    public int getColorCode(){
        return figure.getColorCode();
    }
    public int[][] getShape(){
        return figure.getShape();
    }
    public int getWidth(){
        return figure.getWidth();
    }
    public int getHeight(){
        return figure.getHeight();
    }
}
