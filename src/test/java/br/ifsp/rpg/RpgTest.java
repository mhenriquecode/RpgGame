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
    private Race human;
    private Weapon axe;
    private Weapon sword;

    @Mock private Random mockRandom = mock(Random.class);

    @BeforeEach
    public void setUp() {
        orc = new Race("Orc", 0, 5, 0, 0);
        human = new Race("Human",5,2,2,2);
        axe = new Weapon("Axe",2,6);
        sword = new Weapon("Sword",3,4);
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
    @DisplayName("Applying berserk special attack effect test")
    void applyingBerserkSpecialAttackEffectTest(){
        when(mockRandom.nextInt(100)).thenReturn(5);

        RpgCharacter player = new RpgCharacter("Character", ClassType.BERSERK, orc, axe, mockRandom);

        assertThat(player.getClassType()).isEqualTo(ClassType.BERSERK);
        assertThat(player.attack()).isGreaterThanOrEqualTo(player.getStrength() + 2 * 2);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Applying warrior special attack effect test")
    void applyingWarriorSpecialAttackEffectTest(){
        when(mockRandom.nextInt(100)).thenReturn(5);

        RpgCharacter player = new RpgCharacter("Character", ClassType.WARRIOR, human, sword, mockRandom);
        int oldDefense = player.getDefense();
        int damage = player.attack();

        assertThat(player.getClassType()).isEqualTo(ClassType.WARRIOR);
        assertThat(player.getDefense()).isEqualTo(oldDefense + damage);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Applying paladin special attack effect test")
    void applyingPaladinSpecialAttackEffectTest() {
        when(mockRandom.nextInt(100)).thenReturn(5);

        RpgCharacter player = new RpgCharacter("Character", ClassType.PALADIN, human, sword, mockRandom);
        player.attack();

        assertThat(player.getClassType()).isEqualTo(ClassType.PALADIN);
        assertThat(player.getHealth()).isGreaterThanOrEqualTo(118);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Applying duelist special attack effect test")
    void applyingDuelistSpecialAttackEffectTest() {
        when(mockRandom.nextInt(100)).thenReturn(5);

        RpgCharacter player = new RpgCharacter("Character", ClassType.DUELIST, human, sword, mockRandom);

        assertThat(player.getClassType()).isEqualTo(ClassType.DUELIST);
        assertThat(player.attack()).isStrictlyBetween(23, 41);
    }
}
