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
    public void creatingValidCharacterTest() {
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
    public void calculateCharacterAttributesTest(){
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
    @DisplayName("Must create valid combat between two character")
    void mustCreateValidCombatBetweenTwoCharacters(){
        Race human = new Race("Human", 5, 2, 2, 2);
        Weapon sword = new Weapon("Sword", 3, 4);
        RpgCharacter rpgCharacter1 = new RpgCharacter("Candidor", ClassType.PALADIN, human, sword);

        Race orc = new Race("Orc", 0, 5, 0, 0);
        Weapon axe = new Weapon("Axe", 2, 6);
        RpgCharacter rpgCharacter2 = new RpgCharacter("Matheus", ClassType.BERSERK, orc, axe);

        Combat combat = new Combat(rpgCharacter1, rpgCharacter2);
        assertThat(combat).isNotNull();
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Player one starts the combat when his speed is grater than player two's speed")
    void playerOneWhoStartsTheCombatWhenSpeedIsGreaterThanThePlayerTwoTest(){
        Race human = new Race("Human", 5, 2, 2, 2);
        Weapon sword = new Weapon("Sword", 3, 4);
        RpgCharacter player1 = new RpgCharacter("Candidor", ClassType.PALADIN, human, sword);

        Race orc = new Race("Orc", 0, 5, 0, 1);
        Weapon axe = new Weapon("Axe", 2, 6);
        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, orc, axe);

        Combat combat = new Combat(player1, player2);
        assertThat(player1.getSpeed()).isGreaterThan(player2.getSpeed());

        RpgCharacter first = combat.getFirstToPlay();

        assertThat(first).isEqualTo(player1);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Player two starts the combat when his speed is grater than player one's speed")
    void playerTwoWhoStartsTheCombatWhenSpeedIsGreaterThanThePlayerOneTest(){
        Race human = new Race("Human", 5, 2, 2, 2);
        Weapon sword = new Weapon("Sword", 3, 4);
        RpgCharacter player1 = new RpgCharacter("Candidor", ClassType.PALADIN, human, sword);

        Race orc = new Race("Orc", 0, 5, 0, 3);
        Weapon axe = new Weapon("Axe", 2, 6);
        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, orc, axe);

        Combat combat = new Combat(player1, player2);
        assertThat(player2.getSpeed()).isGreaterThan(player1.getSpeed());

        RpgCharacter first = combat.getFirstToPlay();

        assertThat(first).isEqualTo(player2);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Player one starts when speed is equal to player two's speed")
    void playerOneStartsWhenSpeedIsEqualToThePlayerTwo(){
        Race human = new Race("Human", 5, 2, 2, 2);
        Weapon sword = new Weapon("Sword", 3, 4);
        RpgCharacter player1 = new RpgCharacter("Candidor", ClassType.PALADIN, human, sword);
        Weapon axe = new Weapon("Axe", 2, 6);
        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.PALADIN, human, axe);

        Combat combat = new Combat(player1, player2) {
            @Override
            public int rollD2() {
                return 1;
            }
        };

        RpgCharacter first = combat.getFirstToPlay();
        assertThat(first).isEqualTo(player1);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Player two starts when speed is equal to player one's speed")
    void playerTwoStartsWhenSpeedIsEqualToThePlayerOne(){
        Race human = new Race("Human", 5, 2, 2, 2);
        Weapon sword = new Weapon("Sword", 3, 4);
        RpgCharacter player1 = new RpgCharacter("Candidor", ClassType.PALADIN, human, sword);
        Weapon axe = new Weapon("Axe", 2, 6);
        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.PALADIN, human, axe);

        Combat combat = new Combat(player1, player2) {
            @Override
            public int rollD2() {
                return 2;
            }
        };

        RpgCharacter first = combat.getFirstToPlay();
        assertThat(first).isEqualTo(player2);
    }

}
