package br.ifsp.rpg;
import static org.assertj.core.api.Assertions.*;

import br.ifsp.rpg.model.Race;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.model.Weapon;
import org.junit.jupiter.api.Test;

public class RpgTest {

    @Test
    public void creatingValidCharcaterTest() {
        Weapon axe = new Weapon("Axe", 2, 6);
        Race orc = new Race("Orc", 0, 5, 0, 0);
        RpgCharacter character = new RpgCharacter("Character", "Warrior", orc, axe);

        assertThat(character.getId()).isNotNull();
        assertThat(character.getName()).isEqualTo("Character");
        assertThat(character.getRace().getName()).isEqualTo(orc.getName);
        assertThat(character.getWeapon().name()).isEqualTo(axe.name());
    }
}
