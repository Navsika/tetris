package main.java.com.example.view;

import main.java.com.example.controller.Controller;

import javax.swing.*;
import java.awt.*;

import static main.java.com.example.view.Window.createStyledButton;

public class MainMenu extends JPanel {
    private Controller controller;
    private final JButton newGameButton;
    private final JButton leaderBoardButton;
    private final JButton aboutButton;
    private final JButton exitButton;

    public MainMenu(Controller controller) {
        setLayout(null);
        setBounds(0, 0, Window.width, Window.height);
        setOpaque(true); // panel not unvisible (has color)
        setBackground(new Color(50, 110, 160));

        int buttonWidth = 200;
        int buttonHeight = 50;
        int gap = 20;
        int x = (Window.width - buttonWidth) / 2;
        int startY = 150;

        newGameButton = createStyledButton("New Game");
        newGameButton.setBounds(x, startY, buttonWidth, buttonHeight);

        leaderBoardButton = createStyledButton("Leaderboard");
        leaderBoardButton.setBounds(x, startY + buttonHeight + gap,
                buttonWidth, buttonHeight);

        aboutButton = createStyledButton("About");
        aboutButton.setBounds(x, startY + 2*(buttonHeight + gap),
                buttonWidth, buttonHeight);

        exitButton = createStyledButton("Exit");
        exitButton.setBounds(x, startY + 3*(buttonHeight + gap),
                buttonWidth, buttonHeight);

        newGameButton.addActionListener(e->controller.startGame());
        leaderBoardButton.addActionListener(e->controller.showLeaderboard());
        aboutButton.addActionListener(e->controller.showAbout());
        exitButton.addActionListener(e->System.exit(0));

        add(newGameButton);
        add(leaderBoardButton);
        add(aboutButton);
        add(exitButton);
        revalidate();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}