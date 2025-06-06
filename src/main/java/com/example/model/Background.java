package com.example.model;

public class Background {
    private final int[][] field;
    private final int boardWidth;
    private final int boardHeight;

    public Background(int boardHeight, int boardWidth){
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        field = new int[this.boardHeight][this.boardWidth];
    }

    public boolean resetBackground(ActiveFigure activeFigure) {
        boolean isGameOver = false;
        for (int y = 0; y < activeFigure.getHeight(); y++)
            for (int x = 0; x < activeFigure.getWidth(); x++)
                if (activeFigure.getShape()[y][x] == 1) {
                    int boardX = x + activeFigure.getPosition().x();
                    int boardY = y + activeFigure.getPosition().y();
                    if (boardY < 0) {
                        isGameOver = true;
                        break;
                    }
                    if (boardX < boardWidth && boardX >= 0 && boardY < boardHeight)
                        field[boardY][boardX] = activeFigure.getColorCode();
                }
        return isGameOver;
    }

    public int cleanBackground(){
        int numCleanLines = 0;

        for (int y = boardHeight - 1; y >= 0; y--)
            if (isFullLine(y)){
                deleteLine(y);
                if (y <= boardHeight - 1)
                    shiftLine(y);
                numCleanLines++;
                y++;
            }
        return numCleanLines;
    }

    void deleteLine(int y){
        for (int x = 0; x < boardWidth; x++)
            field[y][x] = 0;
    }

    void shiftLine(int fromY) {
        for (int y = Math.min(fromY - 1, boardHeight - 2); y >= 0; y--) {
            System.arraycopy(field[y], 0, field[y + 1], 0, boardWidth);
        }
        for (int x = 0; x < boardWidth; x++)
            field[0][x] = 0;
    }

    boolean isFullLine(int y){
        for (int x = 0; x < boardWidth; x++){
            if (field[y][x] == 0)
                return false;
        }
        return true;
    }

    public int[][] getField() {
        return field;
    }
}
