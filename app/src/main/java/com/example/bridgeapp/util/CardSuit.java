package com.example.bridgeapp.util;

public enum CardSuit {
    CLUBS("♣"),
    DIAMONDS("♦"),
    HEARTS("♥"),
    SPADES("♠"),
    NOTRUMP("NT");

    public String symbol;

    CardSuit(String symbol){
        this.symbol = symbol;
    }
}