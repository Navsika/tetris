package com.example.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

enum AppState {
    MENU,
    GAME,
    ABOUT,
    LEADERBOARD
}

public class Model {
    public static final int boardWidth = 10;
    public static final int boardHeight = 20;

    private GameThread gameThread;
    private final PropertyChangeSupport pcs;
    private List<PlayerScore> leaderBoard;
    private int lastScore;

    private AppState state;
    public Model(){
        state = AppState.MENU;
        pcs = new PropertyChangeSupport(this);
        leaderBoard = loadLeaderBoard();
        lastScore = 0;
    }

    public void startNewGame() {
        state = AppState.GAME;
        lastScore = 0;
        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new GameThread(this);
            gameThread.start();
        }
        pcs.firePropertyChange("gameStarted", null, true);
    }

    public void moveFigureLeft() {
        if (gameThread != null && gameThread.getActiveFigure() != null) {
            gameThread.getActiveFigure().moveLeft(gameThread.getBackground().getField());
            updateView();
        }
    }

    public void moveFigureRight() {
        if (gameThread != null && gameThread.getActiveFigure() != null) {
            gameThread.getActiveFigure().moveRight(gameThread.getBackground().getField());
            updateView();
        }
    }

    public void setFastDrop(boolean mode){
        if (gameThread != null && gameThread.getActiveFigure() != null){
            gameThread.setFastMode(mode);
        }
    }

    public void rotateFigure(){
        if (gameThread != null && gameThread.getActiveFigure() != null) {
            gameThread.getActiveFigure().turnRight(gameThread.getBackground().getField());
            updateView();
        }
    }

    public void dropFigure() {
        if (gameThread != null && gameThread.getActiveFigure() != null) {
            gameThread.getActiveFigure().moveDown(gameThread.getBackground().getField());
            updateView();
        }
    }

    public void gameOver(){
        lastScore = gameThread != null ? gameThread.getScore() : lastScore;
        state = AppState.MENU;
        pcs.firePropertyChange("gameOver", null, true);
        gameThread = null;
    }

    public void openMenu(){
        state = AppState.MENU;
        pcs.firePropertyChange("menuOpened", null, true);
    }

    public void openLeaderBoard() {
        state = AppState.LEADERBOARD;
        pcs.firePropertyChange("leaderBoardUpdated", null, leaderBoard);
        pcs.firePropertyChange("leaderBoardOpened", null, true);
    }

    public void openAbout() {
        state = AppState.ABOUT;
        pcs.firePropertyChange("aboutOpened", null, true);
    }

    public void addPlayer(String playerName) {
        int score = getGameState().getScore();
        leaderBoard.removeIf(ps -> ps.getPlayerName().equals(playerName));
        leaderBoard.add(new PlayerScore(lastScore, playerName));
        leaderBoard.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        if (leaderBoard.size() > 10) {
            leaderBoard = new ArrayList<>(leaderBoard.subList(0, 10));
        }
        saveLeaderBoard();
        pcs.firePropertyChange("leaderBoardUpdated", null, leaderBoard);
    }

    public void updateView(){
        if (gameThread == null) return;
        lastScore = gameThread.getScore();
        GameState gameState = new GameState(
                gameThread.getBackground().getField(),
                gameThread.getActiveFigure(),
                gameThread.getScore()
        );
        pcs.firePropertyChange("modelUpdated", null, gameState);
    }

    public List<PlayerScore> loadLeaderBoard() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("leaderboard.txt")
        )) {
            List<PlayerScore> loadedList = (List<PlayerScore>) in.readObject();
            return loadedList;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Impossible to load leaderboard.txt. Maybe, it is absent.");
            return new ArrayList<>();
        }
    }

    public void saveLeaderBoard() {
        try (FileOutputStream fs = new FileOutputStream("leaderboard.txt");
            ObjectOutputStream os = new ObjectOutputStream(fs)) {
            os.writeObject(leaderBoard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isInGame() {
        return state == AppState.GAME;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public GameState getGameState() {
        if (gameThread == null) {
            return new GameState(new int[boardHeight][boardWidth], null, 0);
        }
        int[][] field = gameThread.getBackground().getField();
        ActiveFigure figure = gameThread.getActiveFigure();
        int score = gameThread.getScore();
        return new GameState(field, figure, score);
    }
}
