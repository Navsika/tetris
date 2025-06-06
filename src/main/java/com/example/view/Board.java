package com.example.view;

import com.example.model.ActiveFigure;
import com.example.model.GameState;
import com.example.model.Model;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Board extends JPanel implements PropertyChangeListener {
    private final Model model;
    private GameState gameState;
    public static final int width = Model.boardWidth * 30;
    public static final int height = Model.boardHeight * 30;

    public Board(Model model){
        this.model = model;
        this.gameState = new GameState(
                new int[Model.boardHeight][Model.boardWidth], null, 0);
        setBounds(0, 0, width, height);
        setBackground(Color.WHITE);
        model.addPropertyChangeListener(this);
    }

    private void drawCell(Color color, int coordX, int coordY, Graphics g) {
        if (coordY < 0 || coordY >= Model.boardHeight)
            return;
        g.setColor(color);
        g.fillRect(coordX * 30, coordY * 30, 30, 30);
        g.setColor(Color.BLACK);
        g.drawRect(coordX * 30, coordY * 30, 30, 30);
    }

    private Color getColorFromCode(int colorCode){
        return switch (colorCode) {
            case 1 -> Color.CYAN;
            case 2 -> Color.BLUE;
            case 3 -> Color.ORANGE;
            case 4 -> Color.YELLOW;
            case 5 -> Color.GREEN;
            case 6 -> Color.RED;
            case 7 -> Color.MAGENTA;
            default -> Color.WHITE;
        };
    }

    private void drawFigure(ActiveFigure activeFigure, Graphics g){
        if (activeFigure == null)
            return;
        int[][] shape = activeFigure.getShape();
        for (int y = 0; y < activeFigure.getHeight(); y++)
            for (int x = 0; x < activeFigure.getWidth(); x++)
                if (shape[y][x] == 1) {
                    int coordX = x + activeFigure.getPosition().x();
                    int coordY = y + activeFigure.getPosition().y();
                    drawCell(getColorFromCode(activeFigure.getColorCode()), coordX, coordY, g);
                }
    }

    private void drawBackground(Graphics g){
        int[][] field = gameState.getField();
        for (int y = 0; y < Model.boardHeight; y++)
            for (int x = 0; x < Model.boardWidth; x++)
                if (field[y][x] != 0)
                    drawCell(getColorFromCode(field[y][x]), x, y, g);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("modelUpdated".equals(evt.getPropertyName()) && evt.getNewValue() instanceof GameState state) {
            this.gameState = state;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawFigure(gameState.getFigure(), g);
    }
}
