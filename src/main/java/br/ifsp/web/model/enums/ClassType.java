package br.ifsp.web.model.enums;

import lombok.Getter;

@Getter
public enum ClassType {
    BERSERK(20, 5, 0, 0),
    DUELIST(0, 5, 0, 2),
    PALADIN(10, 0, 5, 0),
    WARRIOR(0, 5, 5, 0);

    private final int bonusHealth;
    private final int bonusStrength;
    private final int bonusDefense;
    private final int bonusSpeed;

    ClassType(int bonusHealth, int bonusStrength, int bonusDefense, int bonusSpeed) {
        this.bonusHealth = bonusHealth;
        this.bonusStrength = bonusStrength;
        this.bonusDefense = bonusDefense;
        this.bonusSpeed = bonusSpeed;
    }
}
