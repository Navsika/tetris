package main.java.com.example.model;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Model {
    public static final int boardWidth = 10;
    public static final int boardHeight = 20;
    private GameThread gameThread;
    private final PropertyChangeSupport pcs;
    private List<PlayerScore> leaderBoard;

    private boolean inMenu;
    private boolean inGame;
    private boolean inAbout;
    private boolean inLeaderBoard;

    public Model() {
        inMenu = true;
        inGame = false;
        inAbout = false;
        inLeaderBoard = false;
        pcs = new PropertyChangeSupport(this);
        leaderBoard = loadLeaderBoard();
    }

    public void startNewGame() {
        setStates(false, true, false, false);
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

    public void rotateFigure() {
        if (gameThread != null && gameThread.getActiveFigure() != null) {
            gameThread.getActiveFigure().turnRight(gameThread.getBackground().getField());
            updateView();
        }
    }

    public void dropFigure() {
        if (gameThread != null && gameThread.getActiveFigure() != null) {
            gameThread.getActiveFigure().dropToBottom(gameThread.getBackground().getField());
            updateView();
        }
    }

    public void gameOver() {
        setStates(true, false, false, false);
        gameThread = null;
        pcs.firePropertyChange("gameOver", null, true);
    }

    public void openMenu() {
        setStates(true, false, false, false);
        pcs.firePropertyChange("menuOpened", null, true); // Fixed typo: "munuOpened" to "menuOpened"
    }

    public void openLeaderBoard(){
        inLeaderBoard = true;
        pcs.firePropertyChange("leaderBoardUpdated", null, leaderBoard);
        pcs.firePropertyChange("leaderBoardOpened", null, true);
    }

    public void openAbout() {
        setStates(false, false, true, false);
        pcs.firePropertyChange("aboutOpened", null, true);
    }

    public void addPlayer(String playerName) {
        if (gameThread == null) return;

        int score = gameThread.getScore();
        leaderBoard.removeIf(ps -> ps.getPlayerName().equals(playerName));
        leaderBoard.add(new PlayerScore(score, playerName));
        leaderBoard.sort((p1, p2) -> Integer.compare(p2.getScore(), p1.getScore()));

        if (leaderBoard.size() > 10) {
            leaderBoard = new ArrayList<>(leaderBoard.subList(0, 10));
        }

        saveLeaderBoard();
        pcs.firePropertyChange("leaderBoardUpdated", null, leaderBoard);
    }

    private void setStates(boolean menu, boolean game, boolean about, boolean leaderBoard) {
        this.inMenu = menu;
        this.inGame = game;
        this.inAbout = about;
        this.inLeaderBoard = leaderBoard;
    }

    public List<PlayerScore> loadLeaderBoard() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("leaderboard.txt"))) {
            @SuppressWarnings("unchecked")
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

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void updateView() {
        if (gameThread == null) return;

        GameState state = new GameState(
                gameThread.getBackground().getField(),
                gameThread.getActiveFigure(),
                gameThread.getScore()
        );

        pcs.firePropertyChange("modelUpdated", null, state);
    }

    private GameState getGameState() {
        if (gameThread == null) {
            return new GameState(new int[boardHeight][boardWidth], null, 0);
        }
        int[][] field = gameThread.getBackground().getField();
        ActiveFigure figure = gameThread.getActiveFigure();
        int score = gameThread.getScore();
        return new GameState(field, figure, score);
    }

    public boolean isInGame() {
        return inGame;
    }

    public List<PlayerScore> getLeaderBoard() {
        return leaderBoard;
    }

}