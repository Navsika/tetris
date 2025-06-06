package com.example.view;

import com.example.contorller.Controller;
import com.example.model.PlayerScore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LeaderBoard extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;

    public LeaderBoard(Controller controller) {
        setLayout(null);
        setBounds(0, 0, com.example.view.Window.width, com.example.view.Window.height);
        setBackground(Color.LIGHT_GRAY);

        JButton back = com.example.view.Window.createStyledButton("Back to Menu");
        back.setBounds(com.example.view.Window.width - 200, com.example.view.Window.height - 30, 200, 30);
        back.addActionListener(e->controller.showMenu());
        add(back);

        String[] columns = {"Player", "Score"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 50, com.example.view.Window.width - 100, Window.height - 150);
        add(scrollPane);
    }

    public void updateLeaderBoard(List<PlayerScore> scores){
        tableModel.setRowCount(0);
        for (PlayerScore i: scores)
            tableModel.addRow(new Object[]{i.getPlayerName(), i.getScore()});
    }
}
