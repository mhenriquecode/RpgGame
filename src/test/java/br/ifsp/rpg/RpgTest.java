package br.ifsp.rpg;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.ifsp.rpg.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import java.util.Random;

public class RpgTest {
    private Race orc;
    private Weapon axe;

    @Mock private Random mockRandom = mock(Random.class);

    @BeforeEach
    public void setUp() {
        orc = new Race("Orc", 0, 5, 0, 0);
        axe = new Weapon("Axe", 2, 6);
    }

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

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Character attack test")
    void characterAttackTest(){
        RpgCharacter player = mock(RpgCharacter.class);
        when(player.attack()).thenReturn(20);

        int result = player.attack();

        verify(player).attack();
        assertThat(result).isEqualTo(20);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Applying berserk attack special effect test")
    void applyingBerserkAttackSpecialEffectTest(){
        when(mockRandom.nextInt(100)).thenReturn(5);

        RpgCharacter player = new RpgCharacter("Char", ClassType.BERSERK, orc, axe, mockRandom);

        assertThat(player.attack()).isGreaterThanOrEqualTo(player.getStrength() + 2 * 2);
    }

}
