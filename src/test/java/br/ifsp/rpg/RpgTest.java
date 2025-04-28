package br.ifsp.rpg;
import static org.assertj.core.api.Assertions.*;

import br.ifsp.rpg.model.RpgCharacter;
import org.junit.jupiter.api.Test;

public class RpgTest {

    @Test
    public void creatingCharcaterTest() {
        RpgCharacter character = new RpgCharacter("Character", "Warrior", "Orc", "Axe");

        assertThat(character.getId()).isNotNull();
        assertThat(character.getName()).isEqualTo("Character");
        assertThat(character.getRace()).isEqualTo("Orc");
        assertThat(character.getWeapon()).isEqualTo("Axe");
    }
}
