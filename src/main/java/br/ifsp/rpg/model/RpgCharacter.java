package br.ifsp.rpg.model;

import br.ifsp.rpg.interfaces.SpecialEffect;
import br.ifsp.rpg.model.dice.RollAttackDice;
import br.ifsp.rpg.model.dice.RollHitDice;
import br.ifsp.rpg.model.enums.ClassType;
import br.ifsp.rpg.model.enums.Race;
import br.ifsp.rpg.model.enums.Weapon;
import br.ifsp.rpg.model.specialEffects.SpecialEffectBerserk;
import br.ifsp.rpg.model.specialEffects.SpecialEffectDuelist;
import br.ifsp.rpg.model.specialEffects.SpecialEffectPaladin;
import br.ifsp.rpg.model.specialEffects.SpecialEffectWarrior;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Random;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor(force = true)
public class RpgCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassType classType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Race race;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Weapon weapon;

    private int maxHealth;
    private int health;
    private int strength;
    private int defense;
    private int speed;
    private int armor;
    private boolean defending = false;

    @Transient @JsonIgnore private RollAttackDice attackDice;
    @Transient @JsonIgnore private RollHitDice hitDice;
    @Transient @JsonIgnore private Random random;
    @Transient @JsonIgnore private SpecialEffect specialEffect;

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
        this.random = new Random();
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
    public int rollHitDice(){
        return hitDice.roll();
    }

    public void dodge(){
        armor += speed;
    }

    public void defends(int damageReceived){
        int finalDamage = defending ? Math.max(0, damageReceived - defense) : damageReceived;
        health -= finalDamage;
        defending = false;
    }
}
