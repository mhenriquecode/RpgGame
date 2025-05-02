package br.ifsp.rpg;

import br.ifsp.rpg.interfaces.DiceRoll;
import br.ifsp.rpg.model.dice.RollAttackDice;
import br.ifsp.rpg.model.dice.RollHitDice;
import br.ifsp.rpg.model.enums.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DiceTest {
    private Random mockRandom;

    @BeforeEach
    void setup() {
        mockRandom = mock(Random.class);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("hit dice should return minimum value")
    void hitDiceShouldReturnMinimumValue(){
        when(mockRandom.nextInt(20)).thenReturn(0);

        DiceRoll hitDice = new RollHitDice(mockRandom);
        int result = hitDice.roll();

        assertEquals(1, result);
        verify(mockRandom).nextInt(20);
    }

    @Test
    @Tag("Unit-test")
    @DisplayName("hit dice should return max value")
    void hitDiceShouldReturnMaxValue(){
        when(mockRandom.nextInt(20)).thenReturn(19);

        DiceRoll hitDice = new RollHitDice(mockRandom);
        int result = hitDice.roll();

        assertEquals(20, result);
        verify(mockRandom).nextInt(20);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("should return sum of simulated values for weapon")
    void shouldReturnSumofSimulatedValuesForWeapon(){
        when(mockRandom.nextInt(6)).thenReturn(0, 5);

        DiceRoll attackDice = new RollAttackDice(Weapon.AXE, mockRandom);
        int result = attackDice.roll();

        assertEquals(7, result);
        verify(mockRandom, times(2)).nextInt(6);
    }
}
