package br.ifsp.rpg.model;

import br.ifsp.rpg.interfaces.SpecialEffect;
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

    private RollAttackDice attackDice;
    private RollHitDice hitDice;
    private Random random;
    private SpecialEffect specialEffect;

    public RpgCharacter(String name, ClassType classType, Race race, Weapon weapon) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.classType = classType;
        this.race = race;
        this.weapon = weapon;
        initializeAttributes();
        this.attackDice = new RollAttackDice(this.weapon);
        this.hitDice = new RollHitDice();
        this.specialEffect = chooseSpecialEffect(classType);
    }

    public RpgCharacter(String name, ClassType classType, Race race, Weapon weapon, RollHitDice hitDice, RollAttackDice attackDice) {
        this(name, classType, race, weapon);
        this.hitDice = hitDice;
        this.attackDice = attackDice;
        initializeAttributes();
        this.specialEffect = chooseSpecialEffect(classType);
    }

    public RpgCharacter(String name, ClassType classType, Race race, Weapon weapon, Random random) {
        this(name, classType, race, weapon);
        initializeAttributes();
        this.random = random;
    }

    public void initializeAttributes() {
        this.maxHealth = 100 + race.getBonusHealth() + classType.getBonusHealth();
        this.health = maxHealth;
        this.strength = 10 + race.getBonusStrength() + classType.getBonusStrength();
        this.defense = 5 + race.getBonusDefense() + classType.getBonusDefense();
        this.speed = 5 + race.getBonusSpeed() + classType.getBonusSpeed();
        this.armor = 10;
    }

    private SpecialEffect chooseSpecialEffect(ClassType classType){
        return switch (classType){
            case BERSERK -> new SpecialEffectBerserk();
            case DUELIST -> new SpecialEffectDuelist();
            case PALADIN -> new SpecialEffectPaladin();
            case WARRIOR -> new SpecialEffectWarrior();
        };
    }

    public int attack() {
        int attackDamage = strength + attackDice.roll();

        if(random.nextInt(100) + 1 <= 10) {
            attackDamage = specialEffect.applyEffect(this, attackDamage);
        }

        return attackDamage;
    }

    public int rollAttackDice() {
        return attackDice.roll();
    }

    public void dodge(){
        armor += speed;
    }

    public void defends(int damageReceived){
        int finalDamage = Math.max(0, damageReceived - defense);
        maxHealth -= finalDamage;
    }
}
