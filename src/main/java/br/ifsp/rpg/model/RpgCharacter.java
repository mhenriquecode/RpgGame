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
    private int health;
    private int strength;
    private int defense;
    private int speed;
    private int armor;

    private RollAttackDice attackDie;
    private Random random;

    public RpgCharacter(String name, ClassType classType, Race race, Weapon weapon) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.classType = classType;
        this.race = race;
        this.weapon = weapon;
        initializeAttributes();
        this.attackDie = new RollAttackDice(this.weapon);
    }

    public void initializeAttributes() {
        this.maxHealth = 100 + race.bonusHealth() + classType.getBonusHealth();
        this.health = maxHealth;
        this.strength = 10 + race.bonusStrength() + classType.getBonusStrength();
        this.defense = 5 + race.bonusDefense() + classType.getBonusDefense();
        this.speed = 5 + race.bonusSpeed() + classType.getBonusSpeed();
        this.armor = 10;
    }

    public RpgCharacter(String name, ClassType classType, Race race, Weapon weapon, Random random) {
        this(name, classType, race, weapon);
        this.random = random;
    }

    public int attack() {
        int attackDamage = strength + attackDie.roll();

        if(random.nextInt(100) + 1 <= 10) {
            if(this.classType == ClassType.BERSERK) attackDamage *= 2;
            if(this.classType == ClassType.WARRIOR) this.defense += attackDamage;
            if(this.classType == ClassType.PALADIN) this.health += attackDamage;

        }

        return attackDamage;
    }

    public void dodge(){
        armor += speed;
    }

    public void defends(int damageReceived){
        int finalDamage = Math.max(0, damageReceived - defense);
        maxHealth -= finalDamage;
    }
}
