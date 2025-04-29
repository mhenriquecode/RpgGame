package br.ifsp.rpg.model;

import lombok.Getter;

import java.util.UUID;

public class RpgCharacter {
    @Getter private final UUID id;
    @Getter private String name;
    @Getter private ClassType classType;
    @Getter private Race race;
    @Getter private Weapon weapon;

    public RpgCharacter(String name, ClassType classType, Race race, Weapon weapon) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.classType = classType;
        this.race = race;
        this.weapon = weapon;
    }

}
