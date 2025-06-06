package com.example.model;

import java.io.Serializable;

public class PlayerScore implements Serializable {
    private int score;
    private String playerName;
    public PlayerScore(int score, String playerName){
        this.score = score;
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public String getPlayerName() {
        return playerName;
    }
}
