package br.ifsp.web.dto;

import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

import java.util.UUID;

public record CharacterDTO (
        UUID id,
        @NotBlank String name,
        @NotNull ClassType classType,
        @NotNull Race race,
        @NotNull Weapon weapon,
        Integer maxHealth,
        Integer strength,
        Integer defense,
        Integer speed,
        Integer armor
) {
    public static CharacterDTO from(RpgCharacter character) {
        return new CharacterDTO(
                character.getId(),
                character.getName(),
                character.getClassType(),
                character.getRace(),
                character.getWeapon(),
                character.getMaxHealth(),
                character.getStrength(),
                character.getDefense(),
                character.getSpeed(),
                character.getArmor()
        );
    }

    public RpgCharacter toEntity() {
        return new RpgCharacter(name, classType, race, weapon);
    }
}
