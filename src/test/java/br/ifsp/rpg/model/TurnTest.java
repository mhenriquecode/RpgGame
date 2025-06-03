package br.ifsp.rpg.model;

import br.ifsp.web.dto.TurnLogDTO;
import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.interfaces.PlayerAction;
import br.ifsp.web.interfaces.SpecialEffect;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.Turn;
import br.ifsp.web.model.dice.RollAttackDice;
import br.ifsp.web.model.dice.RollHitDice;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.rpg.stubs.DodgeStub;
import br.ifsp.rpg.stubs.AttackStub;
import br.ifsp.rpg.stubs.DefendingStub;
import br.ifsp.web.model.specialEffects.SpecialEffectBerserk;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TurnTest {

    RpgCharacter current;
    RpgCharacter opponent;
    RollHitDice hitDice;
    RollAttackDice attackDice;

    @BeforeEach
    void setup() {
        current = mock(RpgCharacter.class);
        opponent = mock(RpgCharacter.class);
        hitDice = mock(RollHitDice.class);
        attackDice = mock(RollAttackDice.class);
    }
    @Nested
    @DisplayName("TDD turn tests")
    class TddTurnTests {
        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("Unit-Test")
        @DisplayName("Damage caused by attacker must be the same as defender receives")
        void damageCausedByAttackerMustBeTheSameAsDefenderReceives() {
            RollHitDice hitDiceMock = mock(RollHitDice.class);
            RollAttackDice attackDiceMock = mock(RollAttackDice.class);

            when(hitDiceMock.roll()).thenReturn(20);
            when(attackDiceMock.roll()).thenReturn(4);

            RpgCharacter atacker = new RpgCharacter("Atacante", ClassType.BERSERK, Race.HUMAN, Weapon.HAMMER,
                    hitDiceMock, attackDiceMock);

            RpgCharacter defender = new RpgCharacter("Defensor", ClassType.BERSERK, Race.HUMAN, Weapon.AXE,
                    hitDiceMock, attackDiceMock);

            ChooseAction choosingAttack =  new AttackStub();
            int lifeBeforeAttack = defender.getHealth();

            Turn turnTest = new Turn(atacker, defender, choosingAttack);

            turnTest.execute();

            int damage = lifeBeforeAttack - defender.getHealth();
            assertThat(damage).isIn(21, 42);
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("Unit-Test")
        @DisplayName("attack should not deal damage if hit dice is less than opponent's armor")
        void attackShouldNotDealDamageIfHitFails() {
            RollHitDice hitDiceMock = mock(RollHitDice.class);
            RollAttackDice attackDiceMock = mock(RollAttackDice.class);

            when(hitDiceMock.roll()).thenReturn(5);
            when(attackDiceMock.roll()).thenReturn(15);

            RpgCharacter attacker = new RpgCharacter("Atacante", ClassType.WARRIOR, Race.HUMAN, Weapon.AXE,
                    hitDiceMock, attackDiceMock);

            RpgCharacter defender = new RpgCharacter("Defensor", ClassType.WARRIOR, Race.HUMAN, Weapon.SWORD,
                    hitDiceMock, attackDiceMock);

            int healthBefore = defender.getHealth();

            ChooseAction attackChoose = new AttackStub();
            Turn attackTurn = new Turn(attacker, defender, attackChoose);
            attackTurn.execute();

            assertEquals(healthBefore, defender.getHealth());
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("Unit-Test")
        @DisplayName("defend should reduce damage using defense points")
        void defendShouldReduceDamageUsingDefensePoints() {
            RollHitDice hitDiceMock = mock(RollHitDice.class);
            RollAttackDice attackDiceMock = mock(RollAttackDice.class);

            when(hitDiceMock.roll()).thenReturn(20);
            when(attackDiceMock.roll()).thenReturn(10);

            RpgCharacter attacker = new RpgCharacter("Atacante", ClassType.PALADIN, Race.HUMAN, Weapon.HAMMER,
                    hitDiceMock, attackDiceMock);

            RpgCharacter defender = new RpgCharacter("Defensor", ClassType.PALADIN, Race.HUMAN, Weapon.HAMMER,
                    hitDiceMock, attackDiceMock);

            int initialHealth = defender.getHealth();

            Turn defendTurn = new Turn(defender, attacker, new DefendingStub());
            defendTurn.execute();
            Turn attackTurn = new Turn(attacker, defender, new AttackStub());
            attackTurn.execute();

            int damageTaken = initialHealth - defender.getHealth();

            assertEquals(10, damageTaken);
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("Unit-Test")
        @DisplayName("defend should prevent damage when defense exceeds attack")
        void defendShouldPreventDamageWhenDefenseExceedsAttack() {
            RollHitDice hitDiceMock = mock(RollHitDice.class);
            RollAttackDice attackDiceMock = mock(RollAttackDice.class);

            when(hitDiceMock.roll()).thenReturn(20);
            when(attackDiceMock.roll()).thenReturn(0);

            RpgCharacter attacker = new RpgCharacter("Atacante", ClassType.PALADIN, Race.HUMAN, Weapon.HAMMER,
                    hitDiceMock, attackDiceMock);

            RpgCharacter defender = new RpgCharacter("Defensor", ClassType.PALADIN, Race.HUMAN, Weapon.HAMMER,
                    hitDiceMock, attackDiceMock);

            int initialHealth = defender.getHealth();

            Turn defendTurn = new Turn(defender, attacker, new DefendingStub());
            defendTurn.execute();
            Turn attackTurn = new Turn(attacker, defender, new AttackStub());
            attackTurn.execute();

            int damageTaken = initialHealth - defender.getHealth();

            assertEquals(0, damageTaken);
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("Unit-Test")
        @DisplayName("dodge should increase armor in the turn")
        void dodgeShouldIncreaseArmorInTheTurn() {
            RpgCharacter character = new RpgCharacter("Jogador", ClassType.DUELIST, Race.ELF, Weapon.SWORD);

            int armorBefore = character.getArmor();
            int speedBonus = character.getSpeed();

            ChooseAction dodgeChoose = new DodgeStub();

            RpgCharacter opponent = new RpgCharacter("Inimigo", ClassType.WARRIOR, Race.ORC, Weapon.AXE);

            Turn dodgeTurn = new Turn(character, opponent, dodgeChoose);
            dodgeTurn.execute();

            int expectedArmor = armorBefore + speedBonus;
            assertEquals(expectedArmor, character.getArmor());
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("Unit-Test")
        @DisplayName("dodge Should Increase Armor And Still Take Damage If Hit Dice Exceeds It")
        void dodgeShouldIncreaseArmorAndStillTakeDamageIfHitDiceExceedsIt() {
            RollHitDice hitDiceMock = mock(RollHitDice.class);
            RollAttackDice attackDiceMock = mock(RollAttackDice.class);

            when(hitDiceMock.roll()).thenReturn(17);
            when(attackDiceMock.roll()).thenReturn(5);

            RpgCharacter attacker = new RpgCharacter("Atacante", ClassType.PALADIN, Race.HUMAN, Weapon.HAMMER,
                    hitDiceMock, attackDiceMock);

            RpgCharacter defender = new RpgCharacter("Defensor", ClassType.PALADIN, Race.HUMAN, Weapon.HAMMER,
                    hitDiceMock, attackDiceMock);

            int initialHealth = defender.getHealth();

            Turn dodgeTurn = new Turn(defender, attacker, new DodgeStub());
            dodgeTurn.execute();

            Turn attackTurn = new Turn(attacker, defender, new AttackStub());
            attackTurn.execute();

            int damageTaken = initialHealth - defender.getHealth();
            assertEquals(17, damageTaken);
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("Unit-Test")
        @DisplayName("dodge Bonus Should Expire After Opponents Turn")
        void dodgeBonusShouldExpireAfterOpponentsTurn() {
            RollHitDice hitDiceMock = mock(RollHitDice.class);
            RollAttackDice attackDiceMock = mock(RollAttackDice.class);

            when(hitDiceMock.roll()).thenReturn(17);
            when(attackDiceMock.roll()).thenReturn(10);

            RpgCharacter dodgingPlayer = new RpgCharacter("Jogador", ClassType.DUELIST, Race.ELF, Weapon.SWORD);

            RpgCharacter attacker = new RpgCharacter("Inimigo", ClassType.WARRIOR, Race.ORC, Weapon.AXE,
                    hitDiceMock, attackDiceMock);

            int baseArmor = dodgingPlayer.getArmor();
            int dodgeBonus = dodgingPlayer.getSpeed();
            int expectedArmorWithDodge = baseArmor + dodgeBonus;

            ChooseAction dodgeChoose = new DodgeStub();
            Turn dodgeTurn = new Turn(dodgingPlayer, attacker, dodgeChoose);
            dodgeTurn.execute();

            assertEquals(expectedArmorWithDodge, dodgingPlayer.getArmor());

            int healthBefore = dodgingPlayer.getHealth();
            ChooseAction attackChoose = new AttackStub();
            Turn attackTurn = new Turn(attacker, dodgingPlayer, attackChoose);
            attackTurn.execute();

            assertEquals(healthBefore, dodgingPlayer.getHealth());

            ChooseAction neutralChoose = new AttackStub();
            Turn nextTurn = new Turn(dodgingPlayer, attacker, neutralChoose);
            nextTurn.execute();

            assertEquals(baseArmor, dodgingPlayer.getArmor());
        }

        @Test
        @Tag("Functional")
        @Tag("TDD")
        @Tag("Unit-Test")
        @DisplayName("Dodge should not increase armor above 18")
        void dodgeShouldNotIncreaseArmorAbove18() {
            RpgCharacter character = new RpgCharacter("Jogador", ClassType.DUELIST, Race.ELF, Weapon.SWORD);

            RpgCharacter opponent = new RpgCharacter("Inimigo", ClassType.WARRIOR, Race.ORC, Weapon.AXE);

            ChooseAction dodgeChoose = new DodgeStub();
            new Turn(character, opponent, dodgeChoose).execute();

            assertEquals(18, character.getArmor());
        }
    }
    @Nested
    @DisplayName("Mutation Turn test")
    class MutationTurn {
        @Test
        @Tag("Unit-test")
        @Tag("Mutation")
        @DisplayName("Execute Should Call On New Turn Start And Action Execute")
        void executeShouldCallOnNewTurnStartAndActionExecute() {
            ChooseAction chooseAction = mock(ChooseAction.class);
            PlayerAction action = mock(PlayerAction.class);

            when(chooseAction.choose(current, opponent)).thenReturn(action);

            Turn turn = new Turn(current, opponent, chooseAction);

            turn.execute();

            verify(current, times(1)).onNewTurnStart();
            verify(action, times(1)).execute(current, opponent);
        }
        @Test
        @Tag("Unit-test")
        @Tag("Mutation")
        @DisplayName("Get Turn Log Should Log Attack Miss")
        void getTurnLogShouldLogAttackMiss() {
            when(current.getName()).thenReturn("Atacante");
            when(opponent.getName()).thenReturn("Defensor");

            when(current.getHitDice()).thenReturn(hitDice);
            when(hitDice.getLastRoll()).thenReturn(5);
            when(opponent.getArmor()).thenReturn(10);

            Turn turn = new Turn(current, opponent, (c, o) -> mock(PlayerAction.class));

            TurnLogDTO logDTO = turn.getTurnLog();
            String log = logDTO.actionDescription();

            assertThat(log).contains("Turno de Atacante contra Defensor");
            assertThat(log).contains("Errou o ataque (rolagem de acerto: 5, necessário: 10)");
        }
        @Test
        @Tag("Unit-test")
        @Tag("Mutation")
        @DisplayName("Get Turn Log Should Never Return Null")
        void getTurnLogShouldNeverReturnNull() {
            ChooseAction action = mock(ChooseAction.class);
            when(current.getName()).thenReturn("Atacante");
            when(opponent.getName()).thenReturn("Defensor");

            RollHitDice hitDice = mock(RollHitDice.class);
            when(current.getHitDice()).thenReturn(hitDice);
            when(hitDice.getLastRoll()).thenReturn(1);

            when(opponent.getArmor()).thenReturn(10);

            Turn turn = new Turn(current, opponent, action);

            TurnLogDTO logDTO = turn.getTurnLog();

            assertThat(logDTO).isNotNull();
            assertThat(logDTO.actionDescription()).isNotNull();
        }
        @Test
        @Tag("Unit-test")
        @Tag("Mutation")
        @DisplayName("Get Turn Log Should Log Attack Hit With Special Effect")
        void getTurnLogShouldLogAttackHitWithSpecialEffect() {
            when(current.getName()).thenReturn("Atacante");
            when(opponent.getName()).thenReturn("Defensor");

            when(current.getHitDice()).thenReturn(hitDice);
            when(current.getAttackDice()).thenReturn(attackDice);

            when(hitDice.getLastRoll()).thenReturn(15);
            when(opponent.getArmor()).thenReturn(10);
            when(attackDice.getLastRoll()).thenReturn(7);

            when(current.wasLastSpecialEffectUsed()).thenReturn(true);

            SpecialEffectBerserk specialEffect = new SpecialEffectBerserk();
            when(current.getSpecialEffect()).thenReturn(specialEffect);

            Turn turn = new Turn(current, opponent, (c, o) -> mock(PlayerAction.class));

            TurnLogDTO logDTO = turn.getTurnLog();
            String log = logDTO.actionDescription();

            assertThat(log).contains("Turno de Atacante contra Defensor");
            assertThat(log).contains("Acertou o ataque! Dano causado: 7");
            assertThat(log).contains("Efeito especial");
        }
        @Test
        @Tag("Unit-test")
        @Tag("Mutation")
        @DisplayName("Get Turn Log Should Log Attack Hit When Hit Roll Equals Threshold")
        void getTurnLogShouldLogAttackHitWhenHitRollEqualsThreshold() {
            when(current.getName()).thenReturn("Atacante");
            when(opponent.getName()).thenReturn("Defensor");

            when(current.getHitDice()).thenReturn(hitDice);
            when(current.getAttackDice()).thenReturn(attackDice);

            int hitThreshold = 10;
            when(opponent.getArmor()).thenReturn(hitThreshold);

            // Caso hitRoll exatamente igual ao hitThreshold
            when(hitDice.getLastRoll()).thenReturn(hitThreshold);
            when(attackDice.getLastRoll()).thenReturn(5);

            when(current.wasLastSpecialEffectUsed()).thenReturn(false);
            when(current.getSpecialEffect()).thenReturn(new SpecialEffectBerserk());

            Turn turn = new Turn(current, opponent, (c, o) -> mock(PlayerAction.class));

            TurnLogDTO logDTO = turn.getTurnLog();
            String log = logDTO.actionDescription();

            assertThat(log).contains("Acertou o ataque! Dano causado: 5");
        }
        @Test
        @Tag("Unit-test")
        @Tag("Mutation")
        @DisplayName("Get Turn Log Should Log Attack Miss When Hit Roll Just Below Threshold")
        void getTurnLogShouldLogAttackMissWhenHitRollJustBelowThreshold() {
            when(current.getName()).thenReturn("Atacante");
            when(opponent.getName()).thenReturn("Defensor");

            when(current.getHitDice()).thenReturn(hitDice);
            when(current.getAttackDice()).thenReturn(attackDice);

            int hitThreshold = 10;
            when(opponent.getArmor()).thenReturn(hitThreshold);

            // Caso hitRoll um ponto abaixo do hitThreshold
            when(hitDice.getLastRoll()).thenReturn(hitThreshold - 1);

            Turn turn = new Turn(current, opponent, (c, o) -> mock(PlayerAction.class));

            TurnLogDTO logDTO = turn.getTurnLog();
            String log = logDTO.actionDescription();

            assertThat(log).contains("Errou o ataque");
        }
    }
}
