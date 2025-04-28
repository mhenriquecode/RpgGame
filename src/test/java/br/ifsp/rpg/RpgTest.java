package br.ifsp.rpg;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RpgTest {

    @Test
    public void creatingCharcaterTest() {
        Race orc = new Race("Orc", 0, 5, 0, 0);
        Weapon axe = new Weapon("Axe", 2, 6);
        Character character = new Character("Character", ClassType.WARRIOR, orc, axe);

        assertThat(character.getId()).isNotNull;
        assertThat(character.getName()).isEqualTo("Character");
        assertThat(character.getRace()).isEqualTo(orc);
        assertThat(character.getWeapon()).isEqualTo(axe);
    }
}
