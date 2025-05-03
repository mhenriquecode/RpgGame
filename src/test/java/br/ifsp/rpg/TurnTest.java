package br.ifsp.rpg;

import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.Turn;
import br.ifsp.web.model.dice.RollAttackDice;
import br.ifsp.web.model.dice.RollHitDice;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.rpg.stubs.DodgeStub;
import br.ifsp.rpg.stubs.attackStub;
import br.ifsp.rpg.stubs.defendingStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TurnTest {
    @Test
    @Tag("TDD")
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

        ChooseAction choosingAttack =  new attackStub();
        int lifeBeforeAttack = defender.getHealth();

        Turn turnTest = new Turn(atacker, defender, choosingAttack);

        turnTest.execute();

        int damage = lifeBeforeAttack - defender.getHealth();
        assertEquals(21, damage);
    }

    @Test
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

        ChooseAction attackChoose = new attackStub();
        Turn attackTurn = new Turn(attacker, defender, attackChoose);
        attackTurn.execute();

        assertEquals(healthBefore, defender.getHealth());
    }

    @Test
    @Tag("TDD")
    @DisplayName("defend should reduce damage when choosing to defend")
    void defendShouldReduceDamageWhenChoosingToDefend() {
        RollHitDice hitDiceMock = mock(RollHitDice.class);
        RollAttackDice attackDiceMock = mock(RollAttackDice.class);

        when(hitDiceMock.roll()).thenReturn(20);
        when(attackDiceMock.roll()).thenReturn(10);

        RpgCharacter attacker = new RpgCharacter("Atacante", ClassType.PALADIN, Race.HUMAN, Weapon.HAMMER,
                hitDiceMock, attackDiceMock);

        RpgCharacter defender = new RpgCharacter("Defensor", ClassType.PALADIN, Race.HUMAN, Weapon.HAMMER,
                hitDiceMock, attackDiceMock);

        int initialHealth = defender.getHealth();

        Turn defendTurn = new Turn(defender, attacker, new defendingStub());
        defendTurn.execute();
        Turn attackTurn = new Turn(attacker, defender, new attackStub());
        attackTurn.execute();

        int damageTaken = initialHealth - defender.getHealth();

        assertEquals(10, damageTaken);
    }

    @Test
    @Tag("TDD")
    @DisplayName("dodge should increase armor in the turn")
    void dodgeShouldIncreaseArmorInTheTurn() {
        RpgCharacter character = new RpgCharacter("Jogador", ClassType.DUELIST, Race.ELF, Weapon.SWORD);

        int armorBefore = character.getArmor(); // Valor padrão é 10
        int speedBonus = character.getSpeed();  // speed = 5 (base) + 5 (DUELIST) + 5 (ELF) = 15

        ChooseAction dodgeChoose = new DodgeStub();

        RpgCharacter opponent = new RpgCharacter("Inimigo", ClassType.WARRIOR, Race.ORC, Weapon.AXE);

        Turn dodgeTurn = new Turn(character, opponent, dodgeChoose);
        dodgeTurn.execute();

        int expectedArmor = armorBefore + speedBonus;
        assertEquals(expectedArmor, character.getArmor());
    }

    @Test
    @Tag("TDD")
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
        ChooseAction attackChoose = new attackStub();
        Turn attackTurn = new Turn(attacker, dodgingPlayer, attackChoose);
        attackTurn.execute();

        assertEquals(healthBefore, dodgingPlayer.getHealth());

        ChooseAction neutralChoose = new attackStub();
        Turn nextTurn = new Turn(dodgingPlayer, attacker, neutralChoose);
        nextTurn.execute();

        assertEquals(baseArmor, dodgingPlayer.getArmor());
    }

    @Test
    @Tag("TDD")
    @DisplayName("Dodge should not increase armor above 18")
    void dodgeShouldNotIncreaseArmorAbove18() {
        RpgCharacter character = new RpgCharacter("Jogador", ClassType.DUELIST, Race.ELF, Weapon.SWORD);

        RpgCharacter opponent = new RpgCharacter("Inimigo", ClassType.WARRIOR, Race.ORC, Weapon.AXE);

        ChooseAction dodgeChoose = new DodgeStub();
        new Turn(character, opponent, dodgeChoose).execute();

        assertEquals(18, character.getArmor());
    }


}
