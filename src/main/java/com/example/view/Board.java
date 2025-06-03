package main.java.com.example.view;

import main.java.com.example.model.ActiveFigure;
import main.java.com.example.model.GameState;
import main.java.com.example.model.Model;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Board extends JPanel implements PropertyChangeListener {
    private final Model model;
    private GameState gameState;

    public static final int width = Model.boardWidth * Config.SIZE;
    public static final int height = Model.boardHeight * Config.SIZE;

    public Board(Model model) {
        this.model = model;
        this.gameState = new GameState(new int[Model.boardHeight][Model.boardWidth], null, 0);
        setBounds(0, 0, width, height);
        setBackground(Color.WHITE);
        model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("modelUpdated".equals(evt.getPropertyName()) && evt.getNewValue() instanceof GameState state) {
            System.out.println("GameState figure: " + (state.getFigure() != null) +
                    ", Position: " + (state.getFigure() != null ? state.getFigure().getPosition() : "null"));
            this.gameState = state;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawFigure(gameState.getFigure(), g);
        drawBackground(g);
    }

    private void drawFigure(ActiveFigure figure, Graphics g) {
        if (figure == null) {
            System.out.println("Figure is null, skipping draw");
            return;
        }
        int[][] shape = figure.getShape();
        System.out.println("Drawing figure at x=" + figure.getPosition().x() + ", y=" + figure.getPosition().y() +
                ", shape=");
        for (int y = 0; y < figure.getHeight(); y++) {
            for (int x = 0; x < figure.getWidth(); x++) {
                System.out.print(shape[y][x] + " ");
            }
            System.out.println();
        }
        for (int x = 0; x < figure.getWidth(); x++) {
            for (int y = 0; y < figure.getHeight(); y++) {
                if (shape[y][x] == 1) {
                    int coordX = x + figure.getPosition().x();
                    int coordY = y + figure.getPosition().y();
                    System.out.println("Drawing cell at coordX=" + coordX + ", coordY=" + coordY);
                    drawCell(getColorFromCode(figure.getColorCode()), coordX, coordY, g);
                }
            }
        }
    }

    private void drawCell(Color color, int coordX, int coordY, Graphics g) {
        if (coordY < 0 || coordY >= Model.boardHeight) {
            System.out.println("Skipping cell at coordX=" + coordX + ", coordY=" + coordY + " (out of bounds)");
            return;
        }
        g.setColor(color);
        g.fillRect(coordX * Config.SIZE, coordY * Config.SIZE, Config.SIZE, Config.SIZE);
        g.setColor(Color.BLACK);
        g.drawRect(coordX * Config.SIZE, coordY * Config.SIZE, Config.SIZE, Config.SIZE);
    }

    private void drawBackground(Graphics g) {
        int[][] field = gameState.getField();
        for (int x = 0; x < Model.boardWidth; x++) {
            for (int y = 0; y < Model.boardHeight; y++) {
                if (field[y][x] != 0) {
                    drawCell(getColorFromCode(field[y][x]), x, y, g);
                }
            }
        }
    }

    private Color getColorFromCode(int colorCode) {
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
}