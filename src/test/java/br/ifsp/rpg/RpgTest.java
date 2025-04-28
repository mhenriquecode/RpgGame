package br.ifsp.rpg;
import static org.assertj.core.api.Assertions.*;

import br.ifsp.rpg.model.RpgCharacter;
import org.junit.jupiter.api.Test;

public class RpgTest {

    @Test
    public void creatingValidCharcaterTest() {
        Weapon axe = new Weapon("Axe", 2, 6);
        RpgCharacter character = new RpgCharacter("Character", "Warrior", "Orc", axe);

        assertThat(character.getId()).isNotNull();
        assertThat(character.getName()).isEqualTo("Character");
        assertThat(character.getRace()).isEqualTo("Orc");
        assertThat(character.getWeapon().getName()).isEqualTo(axe.getName());
    }
}
