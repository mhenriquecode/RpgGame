package br.ifsp.rpg;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.ifsp.rpg.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class RpgTest {

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Creating valid character test")
    void creatingValidCharacterTest() {
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

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Calculate character attributes test")
    void calculateCharacterAttributesTest(){
        Weapon sword = new Weapon("Sword", 3, 4);
        Race dwarf = new Race("Dwarf", 20, 0, 5, 0);
        ClassType paladin = ClassType.PALADIN;
        RpgCharacter character = new RpgCharacter("Character", paladin, dwarf, sword);

        assertThat(character.getMaxHealth()).isEqualTo(130);
        assertThat(character.getStrength()).isEqualTo(10);
        assertThat(character.getDefense()).isEqualTo(15);
        assertThat(character.getSpeed()).isEqualTo(5);
    }
    
}
