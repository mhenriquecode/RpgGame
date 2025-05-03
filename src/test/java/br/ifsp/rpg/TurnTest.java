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
    @Tag("TDD")
    @DisplayName("defend should reduce damage when choosing to defend")
    void defendShouldReduceDamageWhenChoosingToDefend() {
        RollHitDice hitDiceMock = mock(RollHitDice.class);
        RollAttackDice attackDiceMock = mock(RollAttackDice.class);

        when(hitDiceMock.roll()).thenReturn(20);
        when(attackDiceMock.roll()).thenReturn(10);

        RpgCharacter atacker = new RpgCharacter("Atacante", ClassType.PALADIN, Race.HUMAN, Weapon.HAMMER,
                hitDiceMock, attackDiceMock);

        RpgCharacter defender = new RpgCharacter("Defensor", ClassType.PALADIN, Race.HUMAN, Weapon.HAMMER,
                hitDiceMock, attackDiceMock);

        int lifeBeforeAttack = defender.getHealth();

        ChooseAction defendingChoose = new defendingStub();
        ChooseAction attackChoose = new attackStub();

        Turn defendingTurn = new Turn(defender, atacker, defendingChoose); // defensor escolhe se defender
        defendingTurn.execute();

        Turn attackTurn = new Turn(atacker, defender, attackChoose); // atacante ataca o defensor
        attackTurn.execute();

        int damage = lifeBeforeAttack - defender.getHealth();

        assertEquals(10, damage);
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
}
