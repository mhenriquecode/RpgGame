package br.ifsp.rpg;
import static org.assertj.core.api.Assertions.*;

import br.ifsp.rpg.model.ClassType;
import br.ifsp.rpg.model.Race;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.model.Weapon;
import org.junit.jupiter.api.Test;

public class RpgTest {

    @Test
    public void creatingValidCharcaterTest() {
        Weapon axe = new Weapon("Axe", 2, 6);
        Race orc = new Race("Orc", 0, 5, 0, 0);
        ClassType warrior = ClassType.WARRIOR;
        RpgCharacter character = new RpgCharacter("Character", warrior, orc, axe);

        assertThat(character.getId()).isNotNull();
        assertThat(character.getName()).isEqualTo("Character");
        assertThat(character.getRace().name()).isEqualTo(orc.name());
        assertThat(character.getWeapon().name()).isEqualTo(axe.name());
        assertThat(character.getClassType()).isEqualTo(warrior);
    }
}
