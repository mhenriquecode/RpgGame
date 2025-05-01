package br.ifsp.rpg.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Random;
import java.util.UUID;

@Getter
@Setter
public class RpgCharacter {
    @Id private final UUID id;
    private String name;
    private ClassType classType;
    private Race race;
    private Weapon weapon;

    private int maxHealth;
    private int strength;
    private int defense;
    private int speed;
    private int armor;

    private RollAttackDice attackDie;
    private Random random = new Random();

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

        this.attackDie = new RollAttackDice(this.weapon);
    }

    public int attack() {
        if (Math.random() < 0.1) {
            return -1;
        }

        return strength + attackDie.roll();
    }
}
