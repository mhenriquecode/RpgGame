package br.ifsp.rpg;

import br.ifsp.rpg.interfaces.ChooseAction;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.model.Turn;
import br.ifsp.rpg.model.actions.ChooseUserAction;
import br.ifsp.rpg.model.dice.RollAttackDice;
import br.ifsp.rpg.model.dice.RollHitDice;
import br.ifsp.rpg.model.enums.ClassType;
import br.ifsp.rpg.model.enums.Race;
import br.ifsp.rpg.model.enums.Weapon;
import br.ifsp.rpg.stubs.attackStub;
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
        assertEquals(14, damage);
    }


}
