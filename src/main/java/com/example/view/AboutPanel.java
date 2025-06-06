package com.example.view;

import com.example.contorller.Controller;

import javax.swing.*;

public class AboutPanel extends JPanel {

    public AboutPanel(Controller controller){
        setLayout(null);
        setBounds(0, 0, Window.width, Window.height);

        JButton back = Window.createStyledButton("Back to Menu");
        back.setBounds(Window.width - 200, Window.height - 30, 200, 30);
        back.addActionListener(e->controller.showMenu());
        add(back);

        String[] scoringRules = {
                "1 line cleared - 1 point",
                "2 lines cleared simultaneously - 5 points",
                "3 lines cleared simultaneously - 20 points",
                "4 lines cleared simultaneously - 100 points"
        };

        for (int i = 0; i < scoringRules.length; i++) {
            JLabel rulesLabel = new JLabel(scoringRules[i]);
            rulesLabel.setBounds(70, 290 + i * 30, 300, 25);
            add(rulesLabel);
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
}
