package br.ifsp.rpg;

import br.ifsp.rpg.interfaces.DiceRoll;
import br.ifsp.rpg.model.RollHitDice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DiceTest {

    @Test
    @DisplayName("hit dice should return minimum value")
    void hitDiceShouldReturnMinimumValue(){
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(20)).thenReturn(0); // +1 => 1

        DiceRoll hitDice = new RollHitDice(mockRandom);
        int result = hitDice.roll();

        assertEquals(1, result);
        verify(mockRandom).nextInt(20);
    }
}
