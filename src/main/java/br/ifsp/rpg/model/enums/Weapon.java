package br.ifsp.rpg.model.enums;

import lombok.Getter;

@Getter
public enum Weapon {
    AXE(2, 6),
    DAGGER(4, 3),
    HAMMER(1, 12),
    SWORD(3, 4);

    final int dice;
    final int sides;

    Weapon(int dice, int sides) {
        this.dice = dice;
        this.sides = sides;
    }
}
