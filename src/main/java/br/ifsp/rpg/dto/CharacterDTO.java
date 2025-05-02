package br.ifsp.rpg.dto;

import br.ifsp.rpg.model.enums.ClassType;
import br.ifsp.rpg.model.enums.Race;
import br.ifsp.rpg.model.enums.Weapon;
import jakarta.validation.constraints.NotBlank;

public record CharacterDTO (String name, ClassType classType, Race race, Weapon weapon) {
}
