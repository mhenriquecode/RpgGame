package br.ifsp.rpg;

import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RpgCharacterTest {
    private RpgCharacter player1;

    @BeforeEach
    void setUp() {
        player1 = new RpgCharacter("Character1", ClassType.PALADIN, Race.HUMAN, Weapon.SWORD);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Creating valid character test")
    void creatingValidCharacterTest() {
        RpgCharacter character = new RpgCharacter("Character", ClassType.WARRIOR, Race.ORC, Weapon.AXE);

        assertThat(character.getId()).isNotNull();
        assertThat(character.getName()).isEqualTo("Character");
        assertThat(character.getRace().name()).isEqualTo(Race.ORC.name());
        assertThat(character.getWeapon().name()).isEqualTo(Weapon.AXE.name());
        assertThat(character.getClassType()).isEqualTo(ClassType.WARRIOR);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Calculate character attributes test")
    void calculateCharacterAttributesTest(){
        RpgCharacter character = new RpgCharacter("Character", ClassType.PALADIN, Race.DWARF, Weapon.SWORD);

        assertThat(character.getMaxHealth()).isEqualTo(130);
        assertThat(character.getStrength()).isEqualTo(10);
        assertThat(character.getDefense()).isEqualTo(15);
        assertThat(character.getSpeed()).isEqualTo(4);
    }

    @Test
    @Tag("Unit-test")
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
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("should increase armor when the character dodges")
    void shouldIncreaseArmorWhenTheCharacterDodges(){
        player1.dodge();
        int resultSpeed = player1.getSpeed();

        assertThat(resultSpeed).isEqualTo(6);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("should decrease damage taken when character defends")
    void shouldDecreaseDamageTakenWhenCharacterDefends(){
        player1.setDefending(true);

        int initialHealth = player1.getHealth();
        int damageReceived = 17;

        player1.defends(damageReceived);
        int expectedDamage = damageReceived - 12;
        int expectedHealth = initialHealth - expectedDamage;

        assertThat(player1.getHealth()).isEqualTo(expectedHealth);
    }

    @Test
    @Tag("Unit-test")
    @DisplayName("User Chooses Elf Then Character Gets Speed Bonus")
    void userChoosesElfThenCharacterGetsSpeedBonus() {
        RpgCharacter character = new RpgCharacter("Legolas", ClassType.WARRIOR, Race.ELF, Weapon.SWORD);
        int totalSpeed = 4 + Race.ELF.getBonusSpeed() + ClassType.WARRIOR.getBonusSpeed(); // 4 + 2 + 0

        assertEquals(totalSpeed, character.getSpeed());
    }

    @Test
    @Tag("Unit-test")
    @DisplayName("User Chooses Dwarf Then Character Receives Life And Defense Bonus")
    void userChoosesDwarfThenCharacterReceivesLifeAndDefenseBonus() {
        RpgCharacter character = new RpgCharacter("Gimli", ClassType.PALADIN, Race.DWARF, Weapon.AXE);

        int totalLife = 100 + Race.DWARF.getBonusHealth() + ClassType.PALADIN.getBonusHealth(); // 100 + 20 + 10
        int totalDefense = 5 + Race.DWARF.getBonusDefense() + ClassType.PALADIN.getBonusDefense(); // 5 + 5 + 5

        assertEquals(totalLife, character.getMaxHealth());
        assertEquals(totalDefense, character.getDefense());
    }

    @Test
    @Tag("Unit-test")
    @DisplayName("User Chooses Human Then Character Receives Moderate Bonus To All Attributes")
    void userChoosesHumanThenCharacterReceivesModerateBonusToAllAttributes() {
        RpgCharacter character = new RpgCharacter("Aragorn", ClassType.BERSERK, Race.HUMAN, Weapon.SWORD);
        int expectedHealth = 100 + Race.HUMAN.getBonusHealth() + ClassType.BERSERK.getBonusHealth(); // 100 + 5 + 20
        int expectedStrength = 10 + Race.HUMAN.getBonusStrength() + ClassType.BERSERK.getBonusStrength(); // 10 + 2 + 5
        int expectedDefense = 5 + Race.HUMAN.getBonusDefense() + ClassType.BERSERK.getBonusDefense(); // 5 + 2 + 0
        int expectedSpeed = 4 + Race.HUMAN.getBonusSpeed() + ClassType.BERSERK.getBonusSpeed(); // 4 + 2 + 0

        assertEquals(expectedHealth, character.getMaxHealth());
        assertEquals(expectedStrength, character.getStrength());
        assertEquals(expectedDefense, character.getDefense());
        assertEquals(expectedSpeed, character.getSpeed());
    }

    @Test
    @Tag("Unit-test")
    @DisplayName("User Chooses Orc Then Character Receives Bonus To Strength")
    void userChoosesOrcThenCharacterReceivesBonusToStrength() {
        RpgCharacter character = new RpgCharacter("Gor", ClassType.DUELIST, Race.ORC, Weapon.AXE);
        int expectedStrength = 10 + Race.ORC.getBonusStrength() + ClassType.DUELIST.getBonusStrength(); // 10 + 5 + 5

        assertEquals(expectedStrength, character.getStrength());
    }
}
