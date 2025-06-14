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
import br.ifsp.web.repository.RpgCharacterEntity;

import java.util.Random;
import java.util.UUID;

public class RpgCharacter {
    private final UUID id;
    private String name;
    private final ClassType classType;
    private final Race race;
    private final Weapon weapon;

    private int maxHealth;
    private int health;
    private int strength;
    private int defense;
    private int speed;
    private int armor;
    private boolean defending = false;
    private boolean hasDodgeBonus = false;

    private RollAttackDice attackDice;
    private RollHitDice hitDice;
    private Random random;
    private SpecialEffect specialEffect;
    private boolean lastSpecialEffectUsed = false;

    public RpgCharacter(String name, ClassType classType, Race race, Weapon weapon) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.classType = classType;
        this.race = race;
        this.weapon = weapon;
        this.maxHealth = 100 + race.getBonusHealth() + classType.getBonusHealth();
        this.health = maxHealth;
        this.strength = 10 + race.getBonusStrength() + classType.getBonusStrength();
        this.defense = 5 + race.getBonusDefense() + classType.getBonusDefense();
        this.speed = 4 + race.getBonusSpeed() + classType.getBonusSpeed();
        this.armor = 10;
        this.attackDice = new RollAttackDice(this.weapon);
        this.hitDice = new RollHitDice();
        this.specialEffect = chooseSpecialEffect(classType);
        this.random = new Random();
    }

    public RpgCharacter(String name, ClassType classType, Race race, Weapon weapon, RollHitDice hitDice, RollAttackDice attackDice) {
        this(name, classType, race, weapon);
        this.hitDice = hitDice;
        this.attackDice = attackDice;
    }

    public RpgCharacter(String name, ClassType classType, Race race, Weapon weapon, Random random) {
        this(name, classType, race, weapon);
        this.random = random;
    }

    public RpgCharacter cloneForCombat() {
        RpgCharacter clone = new RpgCharacter(name, classType, race, weapon);
        clone.setHealth(this.getHealth());
        clone.setStrength(this.getStrength());
        clone.setSpeed(this.getSpeed());
        clone.setDefense(this.getDefense());
        clone.setArmor(this.getArmor());
        return clone;
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
        lastSpecialEffectUsed = false;

        if(random.nextInt(100) + 1 <= 10) {
            attackDamage = specialEffect.applyEffect(this, attackDamage);
            lastSpecialEffectUsed = true;
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
    public boolean wasLastSpecialEffectUsed() {
        return lastSpecialEffectUsed;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null) throw new NullPointerException("Name cannot be null");
        this.name = name;
    }

    public SpecialEffect getSpecialEffect() {
        return specialEffect;
    }

    public RollAttackDice getAttackDice() {
        return attackDice;
    }

    public RollHitDice getHitDice() {
        return hitDice;
    }


    public ClassType getClassType() {
        return classType;
    }

    public Race getRace() {
        return race;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public void setSpecialEffect(SpecialEffect specialEffect) {
        this.specialEffect = specialEffect;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setDefending(boolean defending) {this.defending = defending;}

}