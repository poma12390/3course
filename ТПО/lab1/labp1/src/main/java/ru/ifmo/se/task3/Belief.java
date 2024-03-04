package ru.ifmo.se.task3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Belief {
    private boolean existsGod;
    private Proof proof;
}
