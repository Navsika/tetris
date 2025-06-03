package main.java.com.example.view;

import main.java.com.example.controller.Controller;

import javax.swing.*;
import java.awt.*;

public class AboutPanel extends JPanel {
    private Controller controller;

    public AboutPanel(Controller controller) {
        setLayout(null);
        setBounds(0, 0, Window.width, Window.height);

        this.controller = controller;
        JButton backBtn = Window.createStyledButton("Back to Menu");
        backBtn.setBounds(Window.width - 200, Window.height - 30, 200, 30);
        backBtn.addActionListener(e -> controller.showMenu());
        add(backBtn);

        String[] scoringRules = {
                "1 line cleared - 1 point",
                "2 lines cleared simultaneously - 5 points",
                "3 lines cleared simultaneously - 20 points",
                "4 lines cleared simultaneously - 100 points"
        };

        for (int i = 0; i < scoringRules.length; i++) {
            JLabel ruleLabel = new JLabel(scoringRules[i]);
            ruleLabel.setBounds(70, 290 + i * 30, 300, 25);
            add(ruleLabel);
        }

        String[] controls = {
                "↑ Arrow - Rotate the piece",
                "↓ Arrow - Drop the piece faster",
                "→ Arrow - Move piece right",
                "← Arrow - Move piece left"
        };

        for (int i = 0; i < controls.length; i++) {
            JLabel controlLabel = new JLabel(controls[i]);
            controlLabel.setBounds(70, 120 + i * 30, 300, 25);
            add(controlLabel);
        }

    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}