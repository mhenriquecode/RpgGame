package br.ifsp.rpg;

import br.ifsp.rpg.stubs.AttackStub;
import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.model.Combat;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.actions.ChooseUserAction;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.service.CombatService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class CombatTest {

    private CombatService service;
    private RpgCharacter player1;
    private Random mockRandom;
    private ChooseAction attackAction;

    @BeforeEach
    void setUp() {
        service = new CombatService();
        mockRandom = mock(Random.class);
        attackAction = new AttackStub();
        player1 = new RpgCharacter("Character1", ClassType.PALADIN, Race.HUMAN, Weapon.SWORD);
    }

    @Nested
    @DisplayName("TDD combat test")
    class TddCombatTest {
        @Test
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Must create valid combat between two character")
        void mustCreateValidCombatBetweenTwoCharacters(){
            RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, Race.ORC, Weapon.AXE);
            Combat combat = service.startCombat(player1, attackAction, player2, attackAction);

            assertThat(combat).isNotNull();
        }
        @Test
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Player one starts the combat when his speed is grater than player two's speed")
        void playerOneWhoStartsTheCombatWhenSpeedIsGreaterThanThePlayerTwoTest(){
            RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, Race.ORC, Weapon.AXE);

            assertThat(player1.getSpeed()).isGreaterThan(player2.getSpeed());

            RpgCharacter first = service.getFirstToPlay(player1, player2);

            assertThat(first).isEqualTo(player1);
        }

        @Test
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Player two starts the combat when his speed is grater than player one's speed")
        void playerTwoWhoStartsTheCombatWhenSpeedIsGreaterThanThePlayerOneTest(){
            RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, Race.ORC, Weapon.AXE);

            ChooseAction neutralAction = new ChooseUserAction(1);
            Combat combat = new Combat(player1, neutralAction, player2, neutralAction);

            assertThat(player1.getSpeed()).isGreaterThan(player2.getSpeed());

            RpgCharacter first = service.getFirstToPlay(player1, player2);

            assertThat(first).isEqualTo(player1);
        }

        @Test
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Player one starts when speed is equal to player two's speed")
        void playerOneStartsWhenSpeedIsEqualToThePlayerTwo(){
            when(mockRandom.nextInt(2)).thenReturn(0);

            RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.PALADIN, Race.HUMAN, Weapon.AXE);


            assertThat(player1.getSpeed()).isEqualTo(player2.getSpeed());
            RpgCharacter first = service.getFirstToPlay(player1, player2, mockRandom);

            assertThat(first).isEqualTo(player1);

        }

        @Test
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Player two starts when speed is equal to player one's speed")
        void playerTwoStartsWhenSpeedIsEqualToThePlayerOne(){
            when(mockRandom.nextInt(2)).thenReturn(1);

            RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.PALADIN, Race.HUMAN, Weapon.AXE);

            assertThat(player1.getSpeed()).isEqualTo(player2.getSpeed());
            RpgCharacter first = service.getFirstToPlay(player1, player2, mockRandom);

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
            Combat combat = service.startCombat(player1, neutralAction, player2, neutralAction);

            assertNotNull(combat.getWinner());
        }

        @Test
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("combat determines winner correctly")
        void combatDeterminesWinnerCorrectly() {
            RpgCharacter player1 = new RpgCharacter("Jogador1", ClassType.BERSERK, Race.ORC, Weapon.AXE);

            RpgCharacter player2 = new RpgCharacter("Jogador2", ClassType.DUELIST, Race.ELF, Weapon.DAGGER);

            ChooseAction strategy = new AttackStub();

            Combat combat = service.startCombat(player1, strategy, player2, strategy);

            assertThat(combat.getWinner().getHealth() > 0).isTrue();
            assertThat((combat.getWinner() == player1 ? player2 : player1).getHealth()).isLessThanOrEqualTo(0);
        }
        @Test
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Should create CombatLog with correct values")
        void shouldCreateCombatLogWithCorrectValues() {
            RpgCharacter player2 = new RpgCharacter("candidor", ClassType.BERSERK, Race.ORC, Weapon.DAGGER);

            LocalDateTime now = LocalDateTime.now();
            RpgCharacter winner = player1;
            CombatLog combatLog = new CombatLog(player1, player2, winner);

            assertThat(combatLog).isNotNull();
            assertThat(combatLog.getPlayer1()).isEqualTo(player1);
            assertThat(combatLog.getPlayer2()).isEqualTo(player2);
            assertThat(combatLog.getWinner()).isEqualTo(winner);
            assertThat(combatLog.getTimestamp()).isNotNull();

            assertThat(combatLog.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
        }
    }
    @Nested
    @DisplayName("Unit combat test")
    class UnitCombatTest {
        @Test
        @Tag("Unit-Test")
        @DisplayName("Should not start a combat when one of the characters is invalid test")
        void shouldStartACombatTest(){
            assertThatExceptionOfType(NullPointerException.class)
                    .isThrownBy(() -> service.startCombat(player1, attackAction, null, attackAction));
        }
    }
}
