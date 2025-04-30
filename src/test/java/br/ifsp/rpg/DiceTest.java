package br.ifsp.rpg;

import br.ifsp.rpg.interfaces.DiceRoll;
import br.ifsp.rpg.model.RollHitDice;
import br.ifsp.rpg.model.Weapon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DiceTest {

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("hit dice should return minimum value")
    void hitDiceShouldReturnMinimumValue(){
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(20)).thenReturn(0); // +1 => 1

        DiceRoll hitDice = new RollHitDice(mockRandom);
        int result = hitDice.roll();

        assertEquals(1, result);
        verify(mockRandom).nextInt(20);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("should return sum of simulated values for weapon")
    void shouldReturnSumofSimulatedValuesForWeapon(){
        Weapon weapon = new Weapon("testeWeapn", 2, 6);

        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(6)).thenReturn(0, 5);

        DiceRoll attackDice = new AttackDice(weapon, mockRandom);
        int result = attackDice.roll();

        assertEquals(6, result);
        verify(mockRandom, timeout(2)).nextInt(6);

    }
}
