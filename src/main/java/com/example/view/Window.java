package com.example.view;

import com.example.contorller.Controller;
import com.example.model.Model;
import com.example.model.PlayerScore;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class Window extends JFrame implements PropertyChangeListener {
    private final Model model;
    private Controller controller;
    private GamePanel gamePanel;
    private AboutPanel aboutPanel;
    private MainMenu menuPanel;
    private LeaderBoard leaderBoardPanel;
    public static final int width = Model.boardWidth * 30 * 2;
    public static final int height = Model.boardHeight * 30;

    private int totalWidth;
    private int totalHeight;

    public Window(Model inputModel, Controller inputController){
        this.model = inputModel;
        this.controller = inputController;
        initialize();
    }

    public void initialize(){
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

        menuPanel = new MainMenu(controller);
        aboutPanel = new AboutPanel(controller);
        leaderBoardPanel = new LeaderBoard(controller);

        add(leaderBoardPanel);
        add(aboutPanel);
        add(menuPanel);
        revalidate();
        model.openMenu();
    }

    private void clean(Component component){
        if (component != null){
            component.setVisible(false);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "gameStarted" -> SwingUtilities.invokeLater(this::startGame);
            case "menuOpened" -> SwingUtilities.invokeLater(this::showMenu);
            case "aboutOpened" -> SwingUtilities.invokeLater(this::showAbout);
            case "leaderBoardOpened" -> SwingUtilities.invokeLater(this::showLeaderBoard);
            case "leaderBoardUpdated" -> {
                if (evt.getNewValue() instanceof java.util.List<?> list) {
                    java.util.List<PlayerScore> leaderBoard = (List<PlayerScore>) list;
                    SwingUtilities.invokeLater(() -> {
                        leaderBoardPanel.updateLeaderBoard(leaderBoard);
                    });
                }
            }
            case "gameOver" -> SwingUtilities.invokeLater(this::gameOver);

        }
    }

    public void showMenu(){
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
        clean(gamePanel);
        clean(leaderBoardPanel);

        aboutPanel.setVisible(true);
        repaint();
        aboutPanel.setFocusable(true);
        aboutPanel.requestFocusInWindow();
    }

    public void showLeaderBoard() {
        clean(menuPanel);
        clean(gamePanel);
        clean(aboutPanel);

        leaderBoardPanel.setVisible(true);
        repaint();
        leaderBoardPanel.setFocusable(true);
        leaderBoardPanel.requestFocusInWindow();
    }

    public void startGame(){
        clean(menuPanel);
        clean(aboutPanel);
        clean(leaderBoardPanel);

        gamePanel = new GamePanel(this);
        add(gamePanel);
        revalidate();
        repaint();
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
    }

    public void gameOver(){
        String playerName = JOptionPane.showInputDialog(this, "Game over :)\nPlease enter your name.");
        if (playerName != null && !playerName.trim().isEmpty())
            controller.updateLeaderBoard(playerName);
        model.openMenu();
    }

    public static JButton createStyledButton(String text){
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

    public void setController(Controller controller){
        this.controller = controller;
    }
}
