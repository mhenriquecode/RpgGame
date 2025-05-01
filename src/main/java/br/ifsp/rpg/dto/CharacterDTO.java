package br.ifsp.rpg.dto;

import br.ifsp.rpg.model.ClassType;
import jakarta.validation.constraints.NotBlank;

public class    CharacterDTO {
    @NotBlank private String name;
    @NotBlank private ClassType classType;
    @NotBlank private String race;
    @NotBlank private String weapon;

    public CharacterDTO() {}

    public CharacterDTO(String name, ClassType classType, String race, String weapon) {
        this.name = name;
        this.classType = classType;
        this.race = race;
        this.weapon = weapon;
    }

    public String getName() {
        return name;
    }

    public ClassType getClassType() {
        return classType;
    }

    public String getRace() {
        return race;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }
}
