package br.ifsp.rpg.model;

import lombok.Getter;

import java.util.UUID;

public class RpgCharacter {
    @Getter private UUID id;
    @Getter private String name;
    @Getter private String className;
    @Getter private String race;
    @Getter private String weapon;

    public RpgCharacter(String name, String className, String race, String weapon) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.className = className;
        this.race = race;
        this.weapon = weapon;
    }

}
