package main.java.com.example.view;

import main.java.com.example.controller.Controller;
import main.java.com.example.model.PlayerScore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeaderBoard extends JPanel {
    private Controller controller;
    private DefaultTableModel tableModel;
    private JTable table;
    public LeaderBoard(Controller controller) {
        this.controller = controller;
        setLayout(null);
        setBounds(0, 0, Window.width, Window.height);
        setBackground(Color.LIGHT_GRAY);

        JButton backBtn = Window.createStyledButton("Back to Menu");
        backBtn.setBounds(Window.width - 200, Window.height - 30, 200, 30);
        backBtn.addActionListener(e->controller.showMenu());
        add(backBtn);

        String[] columns = {"Player", "Score"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 50, Window.width - 100, Window.height - 150);

        add(scrollPane);
    }

    public void updateLeaderBoard(List<PlayerScore> scores) {
        tableModel.setRowCount(0);
        for (PlayerScore entry : scores) {
            tableModel.addRow(new Object[]{ entry.getPlayerName(), entry.getScore()});
        }
    }
}