package br.ifsp.rpg;

import br.ifsp.rpg.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Random;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class CombatTest {
    private Race human;
    private Race orc;
    private Weapon sword;
    private Weapon axe;
    private RpgCharacter player1;
    private Random mockRandom;

    @BeforeEach
    void setUp() {
        human = new Race("Human", 5, 2, 2, 2);
        orc = new Race("Orc", 0, 5, 0, 0);
        sword = new Weapon("Sword", 3, 4);
        axe = new Weapon("Axe", 2, 6);
        mockRandom = mock(Random.class);
        player1 = new RpgCharacter("Character1", ClassType.PALADIN, human, sword);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Must create valid combat between two character")
    void mustCreateValidCombatBetweenTwoCharacters(){
        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, orc, axe);
        Combat combat = new Combat(player1, player2);

        assertThat(combat).isNotNull();
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Player one starts the combat when his speed is grater than player two's speed")
    void playerOneWhoStartsTheCombatWhenSpeedIsGreaterThanThePlayerTwoTest(){
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
        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, orc, axe);
        Combat combat = new Combat(player1, player2);

        assertThat(player1.getSpeed()).isGreaterThan(player2.getSpeed());

        RpgCharacter first = combat.getFirstToPlay();

        assertThat(first).isEqualTo(player1);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Player one starts when speed is equal to player two's speed")
    void playerOneStartsWhenSpeedIsEqualToThePlayerTwo(){
        when(mockRandom.nextInt(2)).thenReturn(0);

        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.PALADIN, human, axe);

        Combat combat = new Combat(player1, player2, mockRandom);
        RpgCharacter first = combat.getFirstToPlay();

        assertThat(first).isEqualTo(player1);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Player two starts when speed is equal to player one's speed")
    void playerTwoStartsWhenSpeedIsEqualToThePlayerOne(){
        when(mockRandom.nextInt(2)).thenReturn(1);

        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.PALADIN, human, axe);

        Combat combat = new Combat(player1, player2, mockRandom);
        RpgCharacter first = combat.getFirstToPlay();

        assertThat(first).isEqualTo(player2);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Must create valid turn between one combat with two characters")
    void mustCreateValidTurnBetweenOneCombatWithTwoCharacters(){
        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, orc, axe);
        Turn turn = new Turn(player1, player2);

        assertThat(turn).isNotNull();
    }
}
