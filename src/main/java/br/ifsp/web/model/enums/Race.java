package br.ifsp.web.model.enums;

import lombok.Getter;

@Getter
public enum Race {
    ORC(0, 5, 0, 0),
    DWARF(20, 0, 5, 0),
    ELF(0, 0, 0, 5),
    HUMAN(5, 2, 2, 2);

    final int bonusHealth;
    final int bonusStrength;
    final int bonusDefense;
    final int bonusSpeed;

    Race(int bonusHealth, int bonusStrength, int bonusDefense, int bonusSpeed) {
        this.bonusHealth = bonusHealth;
        this.bonusStrength = bonusStrength;
        this.bonusDefense = bonusDefense;
        this.bonusSpeed = bonusSpeed;
    }
}
