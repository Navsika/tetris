package main.java.com.example.view;

import main.java.com.example.controller.Controller;
import main.java.com.example.model.Model;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private final Window frame;
    private final Model model;
    private final Controller controller;
    private final Board board;
    private final Control control;

    public static final int width = Window.width;
    public static final int height = Window.height;

    public GamePanel(Window window) {
        setLayout(null);
        setBounds(0, 0, width, height);

        this.frame = window;
        this.model = window.getModel();
        this.controller = window.getController();

        board = new Board(model);
        control = new Control(frame);
        add(board);
        add(control);

        System.out.println("Board bounds: " + board.getBounds());
        System.out.println("GamePanel bounds: " + getBounds());

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                controller.handleKeyPress(e);
            }
        });
        setFocusable(true);
        requestFocusInWindow();
    }
}