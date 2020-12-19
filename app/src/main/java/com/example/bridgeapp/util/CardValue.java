package com.example.bridgeapp.util;

public enum CardValue {
    NUM2("2"),
    NUM3("3"),
    NUM4("4"),
    NUM5("5"),
    NUM6("6"),
    NUM7("7"),
    NUM8("8"),
    NUM9("9"),
    NUM10("10"),
    JACK("J"),
    QUEEN("Q"),
    KING("K"),
    ACE("A");

    public String symbol;

    CardValue(String symbol){
        this.symbol = symbol;
    }
}
