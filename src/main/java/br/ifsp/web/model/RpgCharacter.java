package br.ifsp.web.model;

import br.ifsp.web.interfaces.SpecialEffect;
import br.ifsp.web.model.dice.RollAttackDice;
import br.ifsp.web.model.dice.RollHitDice;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.model.specialEffects.SpecialEffectBerserk;
import br.ifsp.web.model.specialEffects.SpecialEffectDuelist;
import br.ifsp.web.model.specialEffects.SpecialEffectPaladin;
import br.ifsp.web.model.specialEffects.SpecialEffectWarrior;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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

    @JsonIgnore private int maxHealth;
    @JsonIgnore private int health;
    @JsonIgnore private int strength;
    @JsonIgnore private int defense;
    @JsonIgnore private int speed;
    @JsonIgnore private int armor;
    @JsonIgnore private boolean defending = false;
    @JsonIgnore private boolean hasDodgeBonus = false;


    @Transient @JsonIgnore private RollAttackDice attackDice;
    @Transient @JsonIgnore private RollHitDice hitDice;
    @Transient @JsonIgnore private Random random;
    @Transient @JsonIgnore private SpecialEffect specialEffect;

    public RpgCharacter(String name, ClassType classType, Race race, Weapon weapon) {
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
        this.speed = 4 + race.getBonusSpeed() + classType.getBonusSpeed();
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
        armor = Math.min(armor + speed, 18);
        hasDodgeBonus = true;
    }

    public void onNewTurnStart() {
        if (hasDodgeBonus) {
            armor -= speed;
            hasDodgeBonus = false;
        }
    }

    public void defends(int damageReceived){
        int finalDamage = defending ? Math.max(0, damageReceived - defense) : damageReceived;
        health -= finalDamage;
        defending = false;
    }
}
