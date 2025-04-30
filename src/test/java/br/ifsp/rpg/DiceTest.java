package br.ifsp.rpg;

import br.ifsp.rpg.interfaces.DiceRoll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiceTest {

    @Test
    @DisplayName("hit dice should return minimum value")
    void hitDiceShouldReturnMinimumValue(){
        DiceRoll mockDice = mock(DiceRoll.class);

        when(mockDice.roll()).thenReturn(1);

        int result = mockDice.roll();
        assertEquals(1, result);
    }
}
