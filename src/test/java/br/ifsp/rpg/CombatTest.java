package br.ifsp.rpg;

import br.ifsp.rpg.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CombatTest {
    Race human;
    Race orc;
    Weapon sword;
    Weapon axe;


    @BeforeEach
    void setUp() {
        human = new Race("Human", 5, 2, 2, 2);
        sword = new Weapon("Sword", 3, 4);
        orc = new Race("Orc", 0, 5, 0, 0);
        axe = new Weapon("Axe", 2, 6);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Must create valid combat between two character")
    void mustCreateValidCombatBetweenTwoCharacters(){
        RpgCharacter rpgCharacter1 = new RpgCharacter("Candidor", ClassType.PALADIN, human, sword);
        RpgCharacter rpgCharacter2 = new RpgCharacter("Matheus", ClassType.BERSERK, orc, axe);
        Combat combat = new Combat(rpgCharacter1, rpgCharacter2);

        assertThat(combat).isNotNull();
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Player one starts the combat when his speed is grater than player two's speed")
    void playerOneWhoStartsTheCombatWhenSpeedIsGreaterThanThePlayerTwoTest(){
        RpgCharacter player1 = new RpgCharacter("Candidor", ClassType.PALADIN, human, sword);
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
        RpgCharacter player1 = new RpgCharacter("Candidor", ClassType.PALADIN, human, sword);
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
        RpgCharacter player1 = new RpgCharacter("Candidor", ClassType.PALADIN, human, sword);
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
        RpgCharacter player1 = new RpgCharacter("Candidor", ClassType.PALADIN, human, sword);
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

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Must create valid turn between one combat with two characters")
    void mustCreateValidTurnBetweenOneCombatWithTwoCharacters(){
        RpgCharacter rpgCharacter1 = new RpgCharacter("Candidor", ClassType.PALADIN, human, sword);
        RpgCharacter rpgCharacter2 = new RpgCharacter("Matheus", ClassType.BERSERK, orc, axe);
        Turn turn = new Turn(rpgCharacter1, rpgCharacter2);
        
        assertThat(turn).isNotNull();
    }

}
