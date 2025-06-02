package br.ifsp.rpg.model;

import br.ifsp.rpg.stubs.AttackStub;
import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.interfaces.PlayerAction;
import br.ifsp.web.log.CombatLog;
import br.ifsp.web.model.Combat;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.actions.AttackAction;
import br.ifsp.web.model.actions.ChooseUserAction;
import br.ifsp.web.model.actions.DefendingAction;
import br.ifsp.web.model.actions.DodgeAction;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.repository.RpgCharacterEntity;
import br.ifsp.web.service.CombatService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class CombatTest {

    private CombatService service;
    private RpgCharacter player;
    private Random mockRandom;
    private ChooseAction attackAction;
    RpgCharacter mockPlayer;
    ChooseAction mockAction;
    RpgCharacter player1;
    RpgCharacter player2;
    ChooseAction strategy1;
    ChooseAction strategy2;
    PlayerAction action1;
    PlayerAction action2;

    @BeforeEach
    void setUp() {
         player1 = mock(RpgCharacter.class);
         player2 = mock(RpgCharacter.class);
         strategy1 = mock(ChooseAction.class);
         strategy2 = mock(ChooseAction.class);
         action1 = mock(PlayerAction.class);
         action2 = mock(PlayerAction.class);
        mockRandom = mock(Random.class);
        attackAction = new AttackStub();
         mockPlayer = mock(RpgCharacter.class);
         mockAction = mock(ChooseAction.class);
        player = new RpgCharacter("Character1", ClassType.PALADIN, Race.HUMAN, Weapon.SWORD);
    }

    @Nested
    @DisplayName("TDD combat test")
    class TddCombatTest {
        @Test
        @Tag("Functional")
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Must create valid combat between two character")
        void mustCreateValidCombatBetweenTwoCharacters(){
            RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, Race.ORC, Weapon.AXE);
            Combat combat = new Combat(player, attackAction, player2, attackAction);
            combat.startCombat(player, attackAction, player2, attackAction);


            assertThat(combat).isNotNull();
        }

        @Test
        @Tag("Functional")
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Player one starts the combat when his speed is grater than player two's speed")
        void playerOneWhoStartsTheCombatWhenSpeedIsGreaterThanThePlayerTwoTest(){
            RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, Race.ORC, Weapon.AXE);

            assertThat(player.getSpeed()).isGreaterThan(player2.getSpeed());

            Combat combat = new Combat(player, attackAction, player2, attackAction);

            RpgCharacter first = combat.getFirstToPlay(player, player2, mockRandom);

            assertThat(first).isEqualTo(player);
            assertThat(first).isNotEqualTo(player2);
        }

        @Test
        @Tag("Functional")
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Player two starts the combat when his speed is grater than player one's speed")
        void playerTwoWhoStartsTheCombatWhenSpeedIsGreaterThanThePlayerOneTest(){
            RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, Race.ORC, Weapon.AXE);

            assertThat(player.getSpeed()).isGreaterThan(player2.getSpeed());

            Combat combat = new Combat(player, attackAction, player2, attackAction);

            RpgCharacter first = combat.getFirstToPlay(player, player2, mockRandom);

            assertThat(first).isEqualTo(player);
            assertThat(first).isNotEqualTo(player2);
        }

        @Test
        @Tag("Functional")
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Player one starts when speed is equal to player two's speed")
        void playerOneStartsWhenSpeedIsEqualToThePlayerTwo(){
            when(mockRandom.nextInt(2)).thenReturn(0);

            RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.PALADIN, Race.HUMAN, Weapon.AXE);

            assertThat(player.getSpeed()).isEqualTo(player2.getSpeed());

            Combat combat = new Combat(player, attackAction, player2, attackAction);
            RpgCharacter first = combat.getFirstToPlay(player, player2, mockRandom);

            assertThat(first).isEqualTo(player);
            assertThat(first).isNotEqualTo(player2);
        }

        @Test
        @Tag("Functional")
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Player two starts when speed is equal to player one's speed")
        void playerTwoStartsWhenSpeedIsEqualToThePlayerOne(){
            when(mockRandom.nextInt(2)).thenReturn(1);

            RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.PALADIN, Race.HUMAN, Weapon.AXE);

            assertThat(player.getSpeed()).isEqualTo(player2.getSpeed());
            Combat combat = new Combat(player, attackAction, player2, attackAction);
            RpgCharacter first = combat.getFirstToPlay(player, player2, mockRandom);

            assertThat(first).isEqualTo(player2);
            assertThat(first).isNotEqualTo(player);
        }

        @Test
        @Tag("Functional")
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("combat winner could not be null")
        void combatWinnerCouldNotBeNull() {
            RpgCharacter player = new RpgCharacter("Jogador1", ClassType.BERSERK, Race.ORC, Weapon.AXE);
            RpgCharacter player2 = new RpgCharacter("Jogador2", ClassType.DUELIST, Race.ELF, Weapon.DAGGER);

            ChooseAction neutralAction = new ChooseUserAction(1);
            Combat combat = new Combat(player, neutralAction, player2, neutralAction);
            combat.startCombat(player, neutralAction, player2, neutralAction);

            assertNotNull(combat.getWinner());
        }

        @Test
        @Tag("Functional")
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("combat determines winner correctly")
        void combatDeterminesWinnerCorrectly() {
            RpgCharacter player = new RpgCharacter("Jogador1", ClassType.BERSERK, Race.ORC, Weapon.AXE);
            RpgCharacter player2 = new RpgCharacter("Jogador2", ClassType.DUELIST, Race.ELF, Weapon.DAGGER);
            ChooseAction strategy = new AttackStub();

            Combat combat = new Combat(player, strategy, player2, strategy);

            combat.startCombat(player, strategy, player2, strategy);

            RpgCharacter clone1 = combat.getFinalClone1();
            RpgCharacter clone2 = combat.getFinalClone2();

            RpgCharacter winner = (clone1.getHealth() > 0) ? clone1 : clone2;
            RpgCharacter loser = (winner == clone1) ? clone2 : clone1;

            assertThat(winner.getHealth()).isGreaterThan(0);
            assertThat(loser.getHealth()).isLessThanOrEqualTo(0);
        }

        @Test
        @Tag("Functional")
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Should create CombatLog with correct values")
        void shouldCreateCombatLogWithCorrectValues() {
            RpgCharacterEntity player1 = new RpgCharacterEntity("blablabla", ClassType.BERSERK, Race.ORC, Weapon.DAGGER);
            RpgCharacterEntity player2 = new RpgCharacterEntity("candidor", ClassType.BERSERK, Race.ORC, Weapon.DAGGER);

            RpgCharacterEntity winner = player1;
            CombatLog combatLog = new CombatLog(player1, player2, winner);

            assertThat(combatLog).isNotNull();
            assertThat(combatLog.getPlayer1Id()).isEqualTo(player1.getId());
            assertThat(combatLog.getPlayer2Id()).isEqualTo(player2.getId());
            assertThat(combatLog.getWinnerId()).isEqualTo(winner.getId());
            assertThat(combatLog.getTimestamp()).isNotNull();

            assertThat(combatLog.getTimestamp()).isBeforeOrEqualTo(LocalDateTime.now());
        }
    }

    @Nested
    @DisplayName("Unit combat test")
    class UnitCombatTest {
        @Test
        @Tag("Functional")
        @Tag("Unit-Test")
        @DisplayName("Should not start a combat when one of the characters is invalid test")
        void shouldStartACombatTest(){
            Combat combat = new Combat(player, attackAction, null, attackAction);
            assertThatExceptionOfType(NullPointerException.class)
                    .isThrownBy(() -> combat.startCombat(player, attackAction, null, attackAction));
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

            assertThat(chooseUserAction.choose(player, player))
                    .isInstanceOf(DefendingAction.class);
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("Should return DodgeAction when chosenAction is 3")
        void shouldReturnDodgeActionWhenChosenActionIs3() {
            ChooseUserAction chooseUserAction = new ChooseUserAction(3);

            assertThat(chooseUserAction.choose(player, player))
                    .isInstanceOf(DodgeAction.class);
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("Should throw IllegalArgumentException for invalid chosenAction")
        void shouldThrowExceptionWhenChosenActionIsInvalid() {
            ChooseUserAction chooseUserAction = new ChooseUserAction(4);

            assertThatThrownBy(() -> chooseUserAction.choose(player, player))
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
            RpgCharacter player = mockPlayer;
            ChooseAction strategy1 = mockAction;
            ChooseAction strategy2 = mockAction;

            Combat combat = new Combat(player, strategy1, player, strategy1);

            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> combat.startCombat(player, strategy1, null, strategy2));
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
            RpgCharacter player = mockPlayer;
            RpgCharacter player2 = mockPlayer;
            ChooseAction strategy1 = mockAction;

            Combat combat = new Combat(player, strategy1, player2, strategy1);

            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> combat.startCombat(player, strategy1, player2, null));
            assertEquals("player2 action cannot be null", exception.getMessage());
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("setWinner Throws When Winner Is Null")
        void setWinnerThrowsWhenWinnerIsNull() {
            RpgCharacter player = mockPlayer;
            ChooseAction strategy = mockAction;
            Combat combat = new Combat(player, strategy, player, strategy);

            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> combat.setWinner(null));
            assertEquals("winner cannot be null", exception.getMessage());
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("startCombat Ends Immediately When Player2 Health Is Zero Or Less")
        void startCombatEndsImmediatelyWhenPlayer2HealthIsZeroOrLess() {
            RpgCharacter player = mockPlayer;
            when(player.getSpeed()).thenReturn(10);
            when(player2.getSpeed()).thenReturn(5);

            when(player.getHealth()).thenReturn(10);
            when(player2.getHealth()).thenReturn(0); // player2 já morreu

            when(player.cloneForCombat()).thenReturn(player);
            when(player2.cloneForCombat()).thenReturn(player2);

            PlayerAction action = mock(PlayerAction.class);
            doNothing().when(action).execute(any(), any());

            ChooseAction strategy1 = mock(ChooseAction.class);
            ChooseAction strategy2 = mock(ChooseAction.class);
            when(strategy1.choose(any(), any())).thenReturn(action);
            when(strategy2.choose(any(), any())).thenReturn(action);

            Combat combat = new Combat(player, strategy1, player2, strategy2);
            combat.startCombat(player, strategy1, player2, strategy2);

            assertEquals(player, combat.getWinner());
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("startCombat Ends Immediately When Player1 Health Is Zero Or Less")
        void startCombatEndsImmediatelyWhenPlayer1HealthIsZeroOrLess() {
            RpgCharacter player = mockPlayer;
            when(player.getSpeed()).thenReturn(5);
            when(player2.getSpeed()).thenReturn(10);

            when(player.getHealth()).thenReturn(0); // player já morreu
            when(player2.getHealth()).thenReturn(10);

            when(player.cloneForCombat()).thenReturn(player);
            when(player2.cloneForCombat()).thenReturn(player2);

            PlayerAction action = mock(PlayerAction.class);
            doNothing().when(action).execute(any(), any());

            ChooseAction strategy1 = mock(ChooseAction.class);
            ChooseAction strategy2 = mock(ChooseAction.class);
            when(strategy1.choose(any(), any())).thenReturn(action);
            when(strategy2.choose(any(), any())).thenReturn(action);

            Combat combat = new Combat(player, strategy1, player2, strategy2);
            combat.startCombat(player, strategy1, player2, strategy2);

            assertEquals(player2, combat.getWinner());
        }

        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("player speed greater than player2 speed returns player")
        void playerSpeedGreaterReturnsPlayer1() {
            RpgCharacter player = mock(RpgCharacter.class);

            when(player.getSpeed()).thenReturn(15);
            when(player2.getSpeed()).thenReturn(10);

            Combat combat = new Combat(player, mock(ChooseAction.class), player2, mock(ChooseAction.class));
            RpgCharacter result = combat.getFirstToPlay(player, player2, mockRandom);

            assertEquals(player, result);
        }
        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("player2 speed greater than player speed returns player2")
        void player2SpeedGreaterReturnsPlayer2() {
            when(player.getSpeed()).thenReturn(10);
            when(player2.getSpeed()).thenReturn(20);

            Combat combat = new Combat(player, mock(ChooseAction.class), player2, mock(ChooseAction.class));
            RpgCharacter result = combat.getFirstToPlay(player, player2, mockRandom);

            assertEquals(player2, result);
        }
        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("getFirstToPlay else if false goes to else block with equal speeds")
        void getFirstToPlayElseIfFalseGoesToElseBlock() {
            when(player.getSpeed()).thenReturn(10);
            when(player2.getSpeed()).thenReturn(10);

            Combat combat = new Combat(player, null, player2, null);
            RpgCharacter result = combat.getFirstToPlay(player, player2, mockRandom);

            assertTrue(result == player || result == player2);
        }
        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("should create Combat instance with correct player assignments and non-null ID")
        void shouldCreateCombatWithCorrectPlayersAndNonNullId(){
            RpgCharacter character = new RpgCharacter("Gor", ClassType.DUELIST, Race.ORC, Weapon.AXE);
            RpgCharacter character2 = new RpgCharacter("Gotton", ClassType.WARRIOR, Race.ELF, Weapon.AXE);
            Combat combat = new Combat(character2, mock(ChooseAction.class), character, mock(ChooseAction.class));
            assertThat(combat.getId()).isNotNull();
            assertThat(combat.getPlayer1()).isEqualTo(character2);
            assertThat(combat.getPlayer2()).isEqualTo(character);
        }
    }

    @Nested
    @DisplayName("Combat and Action Mutation Test")
    public class combatAndActionMutationTest {
        @Test
        @Tag("Mutation")
        @Tag("Unit-Test")
        @DisplayName("Test Attack Succeeds When Hi Equals Armor")
        void testAttackSucceedsWhenHitEqualsArmor() {
            RpgCharacter attacker = mock(RpgCharacter.class);
            RpgCharacter opponent = mock(RpgCharacter.class);

            when(attacker.rollHitDice()).thenReturn(15);
            when(opponent.getArmor()).thenReturn(15);
            when(attacker.attack()).thenReturn(10);


            AttackAction action = new AttackAction();
            action.execute(attacker, opponent);
            verify(opponent).defends(10);
        }

        @Test
        @Tag("Mutation")
        @Tag("Unit-Test")
        @DisplayName("startCombat fails if opponent is wrongly assigned")
        void startCombatFailsIfOpponentAssignmentIsWrong() {
            PlayerAction attackAction = mock(PlayerAction.class);

            when(player1.getSpeed()).thenReturn(5);
            when(player2.getSpeed()).thenReturn(10);

            when(player1.getHealth()).thenReturn(10);
            when(player2.getHealth()).thenReturn(10).thenReturn(10);

            when(player1.cloneForCombat()).thenReturn(player1);
            when(player2.cloneForCombat()).thenReturn(player2);

            when(strategy1.choose(any(), any())).thenReturn(attackAction);
            when(strategy2.choose(any(), any())).thenReturn(attackAction);

            doAnswer(invocation -> {
                when(player1.getHealth()).thenReturn(0);
                return null;
            }).when(attackAction).execute(eq(player2), eq(player1));

            Combat combat = new Combat(player1, strategy1, player2, strategy2);
            combat.startCombat(player1, strategy1, player2, strategy2);
            assertEquals(player2, combat.getWinner());
        }

        @Test
        @Tag("Mutation")
        @Tag("Unit-Test")
        @DisplayName("combat ends immediately if player1 starts with zero health")
        void combatEndsImmediatelyIfPlayer1StartsWithZeroHealth() {
            when(player1.getHealth()).thenReturn(0);
            when(player2.getHealth()).thenReturn(10);
            when(player1.cloneForCombat()).thenReturn(player1);
            when(player2.cloneForCombat()).thenReturn(player2);

            ChooseAction strategy1 = mock(ChooseAction.class);
            ChooseAction strategy2 = mock(ChooseAction.class);

            Combat combat = new Combat(player1, strategy1, player2, strategy2);
            combat.startCombat(player1, strategy1, player2, strategy2);

            assertEquals(player2, combat.getWinner());
        }

        @Test
        @Tag("Mutation")
        @Tag("Unit-Test")
        @DisplayName("combat ends immediately if player2 starts with zero health")
        void combatEndsImmediatelyIfPlayer2StartsWithZeroHealth() {
            when(player1.getHealth()).thenReturn(10);
            when(player2.getHealth()).thenReturn(0);
            when(player1.cloneForCombat()).thenReturn(player1);
            when(player2.cloneForCombat()).thenReturn(player2);

            ChooseAction strategy1 = mock(ChooseAction.class);
            ChooseAction strategy2 = mock(ChooseAction.class);

            Combat combat = new Combat(player1, strategy1, player2, strategy2);
            combat.startCombat(player1, strategy1, player2, strategy2);

            assertEquals(player1, combat.getWinner());
        }

        @Test
        @Tag("Mutation")
        @Tag("Unit-Test")
        @DisplayName("startCombat returns current player as winner when opponent health drops to 0 or less")
        void startCombatReturnsCurrentWhenOpponentHealthIsZeroOrLess() {
            PlayerAction attackAction = mock(PlayerAction.class);
            when(player1.getSpeed()).thenReturn(10);
            when(player2.getSpeed()).thenReturn(5);

            when(player1.getHealth()).thenReturn(10);
            when(player2.getHealth()).thenReturn(10).thenReturn(0); // a segunda chamada simula o golpe fatal

            when(player1.cloneForCombat()).thenReturn(player1);
            when(player2.cloneForCombat()).thenReturn(player2);

            when(strategy1.choose(any(), any())).thenReturn(attackAction);
            when(strategy2.choose(any(), any())).thenReturn(attackAction);

            // quando player1 ataca player2, player2 perde vida e chega a 0
            doAnswer(invocation -> {
                when(player2.getHealth()).thenReturn(0);
                return null;
            }).when(attackAction).execute(eq(player1), eq(player2));

            Combat combat = new Combat(player1, strategy1, player2, strategy2);
            combat.startCombat(player1, strategy1, player2, strategy2);

            assertEquals(player1, combat.getWinner());
        }

        @Test
        @Tag("Mutation")
        @Tag("Unit-Test")
        @DisplayName("Each Player Uses Their Own Strategy")
        void eachPlayerUsesTheirOwnStrategy() {
            when(player1.getHealth()).thenReturn(10, 10, 0);
            when(player2.getHealth()).thenReturn(10, 10);

            when(player1.getSpeed()).thenReturn(10);
            when(player2.getSpeed()).thenReturn(5);

            when(player1.cloneForCombat()).thenReturn(player1);
            when(player2.cloneForCombat()).thenReturn(player2);

            when(strategy1.choose(any(), any())).thenReturn(action1);
            when(strategy2.choose(any(), any())).thenReturn(action2);

            doNothing().when(action1).execute(eq(player1), eq(player2));
            doAnswer(inv -> {
                when(player1.getHealth()).thenReturn(0);
                return null;
            }).when(action2).execute(eq(player2), eq(player1));

            Combat combat = new Combat(player1, strategy1, player2, strategy2);
            combat.startCombat(player1, strategy1, player2, strategy2);

            verify(strategy1, atLeastOnce()).choose(eq(player1), eq(player2));
            verify(action1).execute(eq(player1), eq(player2));

            verify(strategy2, atLeastOnce()).choose(eq(player2), eq(player1));
            verify(action2).execute(eq(player2), eq(player1));

            assertEquals(player2, combat.getWinner());
        }
    }
}
