package main.java.com.example.view;

import main.java.com.example.model.GameState;
import main.java.com.example.model.Model;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Control extends JPanel implements PropertyChangeListener {
    private final Model model;
    private JLabel scoreLabel;
    private GameState gameState;


    public Control(Window frame) {
        this.model = frame.getModel();
        this.gameState = new GameState(new int[Model.boardHeight][Model.boardWidth], null, 0);

        setBounds(Model.boardWidth * Config.SIZE, 0, Config.SIZE * Model.boardWidth, Model.boardHeight * Config.SIZE);
        setBackground(Color.LIGHT_GRAY);
        setLayout(null);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setBounds(20, 20, 150, 30);
        add(scoreLabel);

        model.addPropertyChangeListener(this);
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