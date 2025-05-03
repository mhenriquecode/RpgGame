package br.ifsp.web.dto;

import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
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
