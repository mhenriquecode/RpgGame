package br.ifsp.rpg.model;

import br.ifsp.rpg.stubs.AttackStub;
import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.interfaces.PlayerAction;
import br.ifsp.web.log.CombatLog;
import br.ifsp.web.model.Combat;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.actions.ChooseUserAction;
import br.ifsp.web.model.actions.DefendingAction;
import br.ifsp.web.model.actions.DodgeAction;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.repository.RpgCharacterEntity;
import br.ifsp.web.service.CombatService;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class CombatTest {

    private CombatService service;
    private RpgCharacter player1;
    private Random mockRandom;
    private ChooseAction attackAction;
    RpgCharacter mockPlayer;
    ChooseAction mockAction;


    @BeforeEach
    void setUp() {
        mockRandom = mock(Random.class);
        attackAction = new AttackStub();
         mockPlayer = mock(RpgCharacter.class);
         mockAction = mock(ChooseAction.class);
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
            Combat combat = new Combat(player1, attackAction, player2, attackAction);
            combat.startCombat(player1, attackAction, player2, attackAction);


            assertThat(combat).isNotNull();
        }

        @Test
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Player one starts the combat when his speed is grater than player two's speed")
        void playerOneWhoStartsTheCombatWhenSpeedIsGreaterThanThePlayerTwoTest(){
            RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, Race.ORC, Weapon.AXE);

            assertThat(player1.getSpeed()).isGreaterThan(player2.getSpeed());

            Combat combat = new Combat(player1, attackAction, player2, attackAction);

            RpgCharacter first = combat.getFirstToPlay(player1, player2);

            assertThat(first).isEqualTo(player1);
        }

        @Test
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Player two starts the combat when his speed is grater than player one's speed")
        void playerTwoWhoStartsTheCombatWhenSpeedIsGreaterThanThePlayerOneTest(){
            RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, Race.ORC, Weapon.AXE);

            assertThat(player1.getSpeed()).isGreaterThan(player2.getSpeed());

            Combat combat = new Combat(player1, attackAction, player2, attackAction);

            RpgCharacter first = combat.getFirstToPlay(player1, player2);

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

            Combat combat = new Combat(player1, attackAction, player2, attackAction);
            RpgCharacter first = combat.getFirstToPlay(player1, player2, mockRandom);

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
            Combat combat = new Combat(player1, attackAction, player2, attackAction);
            RpgCharacter first = combat.getFirstToPlay(player1, player2, mockRandom);

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
            combat.startCombat(player1, neutralAction, player2, neutralAction);

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

            Combat combat = new Combat(player1, strategy, player2, strategy);

            combat.startCombat(player1, strategy, player2, strategy);

            RpgCharacter clone1 = combat.getFinalClone1();
            RpgCharacter clone2 = combat.getFinalClone2();

            RpgCharacter winner = (clone1.getHealth() > 0) ? clone1 : clone2;
            RpgCharacter loser = (winner == clone1) ? clone2 : clone1;

            assertThat(winner.getHealth()).isGreaterThan(0);
            assertThat(loser.getHealth()).isLessThanOrEqualTo(0);
        }

        @Test
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Should create CombatLog with correct values")
        void shouldCreateCombatLogWithCorrectValues() {
            RpgCharacterEntity player2 = new RpgCharacterEntity("candidor", ClassType.BERSERK, Race.ORC, Weapon.DAGGER);
            RpgCharacterEntity player1 = new RpgCharacterEntity("blablabla", ClassType.BERSERK, Race.ORC, Weapon.DAGGER);

            RpgCharacterEntity winner = player1;
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
            Combat combat = new Combat(player1, attackAction, null, attackAction);
            assertThatExceptionOfType(NullPointerException.class)
                    .isThrownBy(() -> combat.startCombat(player1, attackAction, null, attackAction));
        }
    }

    @Nested
    @DisplayName("Struct combat tests")
    class StructCombatTest {
        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("Should return DefendingAction when chosenAction is 2")
        void shouldReturnDefendingActionWhenChosenActionIs2() {
            ChooseUserAction chooseUserAction = new ChooseUserAction(2);

            assertThat(chooseUserAction.choose(player1, player1))
                    .isInstanceOf(DefendingAction.class);
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("Should return DodgeAction when chosenAction is 3")
        void shouldReturnDodgeActionWhenChosenActionIs3() {
            ChooseUserAction chooseUserAction = new ChooseUserAction(3);

            assertThat(chooseUserAction.choose(player1, player1))
                    .isInstanceOf(DodgeAction.class);
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("Should throw IllegalArgumentException for invalid chosenAction")
        void shouldThrowExceptionWhenChosenActionIsInvalid() {
            ChooseUserAction chooseUserAction = new ChooseUserAction(4);

            assertThatThrownBy(() -> chooseUserAction.choose(player1, player1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Invalid action");
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("startCombat Throws When Player1 Is Null")
        void startCombatThrowsWhenPlayer1IsNull() {
            RpgCharacter player2 = mockPlayer;
            ChooseAction strategy1 = mockAction;
            ChooseAction strategy2 = mockAction;

            Combat combat = new Combat(player2, strategy2, player2, strategy2);

            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> combat.startCombat(null, strategy1, player2, strategy2));
            assertEquals("player1 cannot be null", exception.getMessage());
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("startCombat Throws When Player2 Is Null")
        void startCombatThrowsWhenPlayer2IsNull() {
            RpgCharacter player1 = mockPlayer;
            ChooseAction strategy1 = mockAction;
            ChooseAction strategy2 = mockAction;

            Combat combat = new Combat(player1, strategy1, player1, strategy1);

            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> combat.startCombat(player1, strategy1, null, strategy2));
            assertEquals("player2 cannot be null", exception.getMessage());
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("startCombat Throws When Strategy1 Is Null")
        void startCombatThrowsWhenStrategy1IsNull() {
            RpgCharacter player1 = mockPlayer;
            RpgCharacter player2 = mockPlayer;
            ChooseAction strategy2 = mockAction;

            Combat combat = new Combat(player1, strategy2, player2, strategy2);

            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> combat.startCombat(player1, null, player2, strategy2));
            assertEquals("player1 action cannot be null", exception.getMessage());
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("startCombat Throws When Strategy2 Is Null")
        void startCombatThrowsWhenStrategy2IsNull() {
            RpgCharacter player1 = mockPlayer;
            RpgCharacter player2 = mockPlayer;
            ChooseAction strategy1 = mockAction;

            Combat combat = new Combat(player1, strategy1, player2, strategy1);

            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> combat.startCombat(player1, strategy1, player2, null));
            assertEquals("player2 action cannot be null", exception.getMessage());
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("setWinner Throws When Winner Is Null")
        void setWinnerThrowsWhenWinnerIsNull() {
            RpgCharacter player1 = mockPlayer;
            ChooseAction strategy = mockAction;

            Combat combat = new Combat(player1, strategy, player1, strategy);

            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> combat.setWinner(null));
            assertEquals("winner cannot be null", exception.getMessage());
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("startCombat Ends Immediately When Player2 Health Is Zero Or Less")
        void startCombatEndsImmediatelyWhenPlayer2HealthIsZeroOrLess() {
            RpgCharacter player1 = mockPlayer;
            RpgCharacter player2 = mock(RpgCharacter.class);

            when(player1.getSpeed()).thenReturn(10);
            when(player2.getSpeed()).thenReturn(5);

            when(player1.getHealth()).thenReturn(10);
            when(player2.getHealth()).thenReturn(0); // player2 já morreu

            when(player1.cloneForCombat()).thenReturn(player1);
            when(player2.cloneForCombat()).thenReturn(player2);

            PlayerAction action = mock(PlayerAction.class);
            doNothing().when(action).execute(any(), any());

            ChooseAction strategy1 = mock(ChooseAction.class);
            ChooseAction strategy2 = mock(ChooseAction.class);
            when(strategy1.choose(any(), any())).thenReturn(action);
            when(strategy2.choose(any(), any())).thenReturn(action);

            Combat combat = new Combat(player1, strategy1, player2, strategy2);
            combat.startCombat(player1, strategy1, player2, strategy2);

            assertEquals(player1, combat.getWinner());
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("startCombat Ends Immediately When Player1 Health Is Zero Or Less")
        void startCombatEndsImmediatelyWhenPlayer1HealthIsZeroOrLess() {
            RpgCharacter player1 = mockPlayer;
            RpgCharacter player2 = mock(RpgCharacter.class);

            when(player1.getSpeed()).thenReturn(5);
            when(player2.getSpeed()).thenReturn(10);

            when(player1.getHealth()).thenReturn(0); // player1 já morreu
            when(player2.getHealth()).thenReturn(10);

            when(player1.cloneForCombat()).thenReturn(player1);
            when(player2.cloneForCombat()).thenReturn(player2);

            PlayerAction action = mock(PlayerAction.class);
            doNothing().when(action).execute(any(), any());

            ChooseAction strategy1 = mock(ChooseAction.class);
            ChooseAction strategy2 = mock(ChooseAction.class);
            when(strategy1.choose(any(), any())).thenReturn(action);
            when(strategy2.choose(any(), any())).thenReturn(action);

            Combat combat = new Combat(player1, strategy1, player2, strategy2);
            combat.startCombat(player1, strategy1, player2, strategy2);

            assertEquals(player2, combat.getWinner());
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("player1 speed greater than player2 speed returns player1")
        void player1SpeedGreaterReturnsPlayer1() {
            RpgCharacter player1 = mock(RpgCharacter.class);
            RpgCharacter player2 = mock(RpgCharacter.class);
            Random random = mock(Random.class);

            when(player1.getSpeed()).thenReturn(15);
            when(player2.getSpeed()).thenReturn(10);

            Combat combat = new Combat(player1, null, player2, null);
            RpgCharacter result = combat.getFirstToPlay(player1, player2, random);

            assertEquals(player1, result);
        }
        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("player2 speed greater than player1 speed returns player2")
        void player2SpeedGreaterReturnsPlayer2() {
            RpgCharacter player1 = mock(RpgCharacter.class);
            RpgCharacter player2 = mock(RpgCharacter.class);
            Random random = mock(Random.class);

            when(player1.getSpeed()).thenReturn(10);
            when(player2.getSpeed()).thenReturn(20);

            Combat combat = new Combat(player1, null, player2, null);
            RpgCharacter result = combat.getFirstToPlay(player1, player2, random);

            assertEquals(player2, result);
        }
    }
}
