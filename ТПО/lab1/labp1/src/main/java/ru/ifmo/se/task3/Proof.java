package ru.ifmo.se.task3;

import lombok.Getter;

@Getter
public class Proof {
    private Rarity rarity;
    private String description;

    public Proof(Rarity rarity) {
        this.rarity = rarity;
    }
}
