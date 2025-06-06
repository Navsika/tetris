package com.example.view;

import com.example.model.GameState;
import com.example.model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Control extends JPanel implements PropertyChangeListener {
    private final Model model;
    private JLabel scoreLabel;
    private GameState gameState;
    public static final int width = Window.width - Board.width;
    public static final int height = Board.height;

    public Control(Window frame, ActionListener actionListener) {
        model = frame.getModel();
        this.gameState = new GameState(
                new int[Model.boardHeight][Model.boardWidth], null, 0);

        setBounds(Model.boardWidth * 30, 0, Model.boardWidth * 30, Model.boardHeight * 30);
        setBackground(Color.LIGHT_GRAY);
        setLayout(null);

        model.addPropertyChangeListener(this);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setBounds(20, 20, 150, 30);
        add(scoreLabel);

        JButton back = Window.createStyledButton("Back to Menu");
        back.setBounds((width - 200) / 2, height - height / 4, 200, 30);
        back.addActionListener(actionListener);
        add(back);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("modelUpdated".equals(evt.getPropertyName()) && evt.getNewValue() instanceof GameState state) {
            this.gameState = state;
            scoreLabel.setText("Score: " + gameState.getScore());
            repaint();
        }
    }
}
