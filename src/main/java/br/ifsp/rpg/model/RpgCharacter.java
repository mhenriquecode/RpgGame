package br.ifsp.rpg.model;

import lombok.Getter;

import java.util.UUID;

public class RpgCharacter {
    @Getter private final UUID id;
    @Getter private String name;
    @Getter private ClassType classType;
    @Getter private Race race;
    @Getter private Weapon weapon;

    @Getter private int maxHealth;
    @Getter private int strength;
    @Getter private int defense;
    @Getter private int speed;
    @Getter private int armor;

    public RpgCharacter(String name, ClassType classType, Race race, Weapon weapon) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.classType = classType;
        this.race = race;
        this.weapon = weapon;

        this.maxHealth = 100 + race.bonusHealth() + classType.getBonusHealth();
        this.strength = 10 + race.bonusStrength() + classType.getBonusStrength();
        this.defense = 5 + race.bonusDefense() + classType.getBonusDefense();
        this.speed = 5 + race.bonusSpeed() + classType.getBonusSpeed();
        this.armor = 10;
    }





}
