package br.ifsp.rpg.dto;

import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.model.enums.ClassType;
import br.ifsp.rpg.model.enums.Race;
import br.ifsp.rpg.model.enums.Weapon;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record CharacterDTO (
        @NotBlank String name,
        @NonNull ClassType classType,
        @NonNull Race race,
        @NonNull Weapon weapon)
{
    public static CharacterDTO from(RpgCharacter character) {
        return new CharacterDTO(
                character.getName(),
                character.getClassType(),
                character.getRace(),
                character.getWeapon()
        );
    }
}
