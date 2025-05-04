package br.ifsp.rpg;

import br.ifsp.rpg.stubs.attackStub;
import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.model.Combat;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.actions.ChooseUserAction;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.service.CombatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class CombatTest {

    private RpgCharacter player1;
    private Random mockRandom;

    private CombatService combatService;

    @BeforeEach
    void setUp() {
        mockRandom = mock(Random.class);
        combatService = new CombatService();
        player1 = new RpgCharacter("Character1", ClassType.PALADIN, Race.HUMAN, Weapon.SWORD);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Must create valid combat between two character")
    void mustCreateValidCombatBetweenTwoCharacters(){
        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, Race.ORC, Weapon.AXE);
        Combat combat = combatService.startCombat(player1, player2);

        assertThat(combat).isNotNull();
    }

    @Test
    @Tag("Unit-Test")
    @DisplayName("Should not start a combat when one of the characters is invalid test")
    void shouldStartACombatTest(){
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> combatService.startCombat(player1, null));
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Player one starts the combat when his speed is grater than player two's speed")
    void playerOneWhoStartsTheCombatWhenSpeedIsGreaterThanThePlayerTwoTest(){
        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, Race.ORC, Weapon.AXE);
        ChooseAction neutralAction = new ChooseUserAction(1);
        Combat combat = new Combat(player1, neutralAction, player2, neutralAction);

        assertThat(player1.getSpeed()).isGreaterThan(player2.getSpeed());

        RpgCharacter first = combat.getFirstToPlay();

        assertThat(first).isEqualTo(player1);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Player two starts the combat when his speed is grater than player one's speed")
    void playerTwoWhoStartsTheCombatWhenSpeedIsGreaterThanThePlayerOneTest(){
        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, Race.ORC, Weapon.AXE);

        ChooseAction neutralAction = new ChooseUserAction(1);
        Combat combat = new Combat(player1, neutralAction,player2, neutralAction);

        assertThat(player1.getSpeed()).isGreaterThan(player2.getSpeed());

        RpgCharacter first = combat.getFirstToPlay();

        assertThat(first).isEqualTo(player1);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Player one starts when speed is equal to player two's speed")
    void playerOneStartsWhenSpeedIsEqualToThePlayerTwo(){
        when(mockRandom.nextInt(2)).thenReturn(0);

        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.PALADIN, Race.HUMAN, Weapon.AXE);

        Combat combat = new Combat(player1, player2, mockRandom);
        RpgCharacter first = combat.getFirstToPlay();

        assertThat(first).isEqualTo(player1);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Player two starts when speed is equal to player one's speed")
    void playerTwoStartsWhenSpeedIsEqualToThePlayerOne(){
        when(mockRandom.nextInt(2)).thenReturn(1);

        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.PALADIN, Race.HUMAN, Weapon.AXE);

        Combat combat = new Combat(player1, player2, mockRandom);
        RpgCharacter first = combat.getFirstToPlay();

        assertThat(first).isEqualTo(player2);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("combat winner could not be null")
    void combatWinnerCouldNotBeNull() {
        RpgCharacter player1 = new RpgCharacter("Jogador1", ClassType.BERSERK, Race.ORC, Weapon.AXE);

        RpgCharacter player2 = new RpgCharacter("Jogador2", ClassType.DUELIST, Race.ELF, Weapon.DAGGER);

        ChooseAction neutralAction = new ChooseUserAction(1);
        Combat combat = new Combat(player1, neutralAction, player2, neutralAction);
        combat.start();

        assertNotNull(combat.getWinner());
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("combat determines winner correctly")
    void combatDeterminesWinnerCorrectly() {
        RpgCharacter player1 = new RpgCharacter("Jogador1", ClassType.BERSERK, Race.ORC, Weapon.AXE);

        RpgCharacter player2 = new RpgCharacter("Jogador2", ClassType.DUELIST, Race.ELF, Weapon.DAGGER);

        ChooseAction strategy = new attackStub();

        Combat combat = new Combat(player1, strategy, player2, strategy);
        combat.start();

        assertThat(combat.getWinner().getHealth() > 0).isTrue();
        assertThat((combat.getWinner() == player1 ? player2 : player1).getHealth()).isLessThanOrEqualTo(0);
    }
}
