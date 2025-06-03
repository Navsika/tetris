package main.java.com.example.view;

import main.java.com.example.controller.Controller;
import main.java.com.example.model.Model;
import main.java.com.example.model.PlayerScore;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class Window extends JFrame implements PropertyChangeListener {
    private final Model model;
    private final Controller controller;

    private GamePanel gamePanel;
    private AboutPanel aboutPanel;
    private MainMenu menuPanel;
    private LeaderBoard leaderBoardPanel;
    public static final int width = Model.boardWidth * Config.SIZE * 2;
    public static final int height = Model.boardHeight * Config.SIZE;
    private int totalWidth;
    private int totalHeight;

    public Window(Model inputModel, Controller inputController) {
        this.model = inputModel;
        this.controller = inputController;
        initialize();
    }

    public void initialize() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setTitle("Tetris");
        setLayout(null);
        setResizable(false);

        Insets insets = getInsets();
        totalWidth = width + insets.left + insets.right;
        totalHeight = height + insets.top + insets.bottom;
        setSize(totalWidth, totalHeight);
        setLocationRelativeTo(null);

        model.addPropertyChangeListener(this);

        leaderBoardPanel = new LeaderBoard(controller);
        menuPanel = new MainMenu(controller);
        aboutPanel = new AboutPanel(controller);

        add(leaderBoardPanel);
        add(menuPanel);
        add(aboutPanel);
        revalidate();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "gameStarted" -> SwingUtilities.invokeLater(this::startGame);
            case "menuOpened" -> SwingUtilities.invokeLater(this::showMenu);
            case "aboutOpened" -> SwingUtilities.invokeLater(this::showAbout);
            case "leaderBoardOpened" -> SwingUtilities.invokeLater(this::showAbout);

            case "leaderBoardUpdated" -> {
                if (evt.getNewValue() instanceof List<?> list) {
                    @SuppressWarnings("unchecked")
                    List<PlayerScore> leaderBoard = (List<PlayerScore>) list;
                    SwingUtilities.invokeLater(() -> {
                        leaderBoardPanel.updateLeaderBoard(leaderBoard);
                    });
                }
            }
            case "gameOver" -> SwingUtilities.invokeLater(this::gameOver);
        }
    }

    public void showMenu() {
        clean(gamePanel);
        clean(leaderBoardPanel);
        clean(aboutPanel);
        menuPanel.setVisible(true);
        repaint();
        menuPanel.setFocusable(true);
        menuPanel.requestFocusInWindow();
    }

    public void showAbout() {

        clean(menuPanel);

        aboutPanel.setVisible(true);
        repaint();
        aboutPanel.setFocusable(true);
        aboutPanel.requestFocusInWindow();
    }

    public void showLeaderboard() {
        clean(menuPanel);

        leaderBoardPanel.setVisible(true);
        repaint();
        leaderBoardPanel.setFocusable(true);
        leaderBoardPanel.requestFocusInWindow();
    }

    public void startGame() {
        clean(menuPanel);

        gamePanel = new GamePanel(this);
        add(gamePanel);
        revalidate();
        repaint();
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
    }

    public void gameOver() {
        String playerName = JOptionPane.showInputDialog(this, "Game over :)\nPlease enter your name.");
        controller.updateLeaderBoard(playerName);
        dispose();
    }

    private void clean(Component object) {
        if (object != null) {
            object.setVisible(false);
        }
    }

    public static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        return button;
    }

    public Model getModel() {
        return model;
    }

    public Controller getController() {
        return controller;
    }

    public int getTotalHeight() {
        return totalHeight;
    }

    public int getTotalWidth() {
        return totalWidth;
    }
}