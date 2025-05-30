package br.ifsp.rpg.model;

import br.ifsp.web.interfaces.DiceRoll;
import br.ifsp.web.model.dice.RollAttackDice;
import br.ifsp.web.model.dice.RollHitDice;
import br.ifsp.web.model.enums.Weapon;
import org.junit.jupiter.api.*;

import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DiceTest {
    private Random mockRandom;

    @BeforeEach
    void setup() {
        mockRandom = mock(Random.class);
    }


    @Nested
    @DisplayName("TDD dice test")
    class TddDiceTest {
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
        @Tag("TDD")
        @DisplayName("should return sum of simulated values for axe weapon")
        void shouldReturnSumofSimulatedValuesForAxeWeapon(){
            when(mockRandom.nextInt(6)).thenReturn(0, 5);

            DiceRoll attackDice = new RollAttackDice(Weapon.AXE, mockRandom);
            int result = attackDice.roll();

            assertEquals(7, result);
            verify(mockRandom, times(2)).nextInt(6);
        }
    }

    @Nested
    @DisplayName("Unit dice test")
    class UnitDiceTest {
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
        @DisplayName("should return simulated value for hammer weapon")
        void shouldReturnSimulatedValueForHammerWeapon() {
            when(mockRandom.nextInt(12)).thenReturn(6);

            DiceRoll attackDice = new RollAttackDice(Weapon.HAMMER, mockRandom);
            int result = attackDice.roll();

            assertEquals(7, result);
            verify(mockRandom, times(1)).nextInt(12);
        }
        @Test
        @Tag("Unit-test")
        @DisplayName("should return simulated value for sword weapon")
        void shouldReturnSimulatedValueForSwordWeapon() {
            when(mockRandom.nextInt(4)).thenReturn(3);

            DiceRoll attackDice = new RollAttackDice(Weapon.SWORD, mockRandom);
            int result = attackDice.roll();

            assertEquals(12, result);
            verify(mockRandom, times(3)).nextInt(4);
        }

        @Test
        @Tag("Unit-test")
        @DisplayName("should return simulated value for dagger weapon")
        void shouldReturnSimulatedValueForDaggerWeapon() {
            when(mockRandom.nextInt(3)).thenReturn(2);

            DiceRoll attackDice = new RollAttackDice(Weapon.DAGGER, mockRandom);
            int result = attackDice.roll();

            assertEquals(12, result);
            verify(mockRandom, times(4)).nextInt(3);
        }
    }
    @Nested
    @DisplayName("Struct dice tests")
    class StructDiceTest {
        @Test
        @Tag("Structural")
        @Tag("Unit-Test")
        @DisplayName("should return zero if weapon dice is zero")
        void shouldReturnZeroIfWeaponDiceIsZero() {
            Weapon weaponMock = mock(Weapon.class);
            when(weaponMock.getDice()).thenReturn(0);
            when(weaponMock.getSides()).thenReturn(6);

            DiceRoll attackDice = new RollAttackDice(weaponMock, mockRandom);
            int result = attackDice.roll();

            assertThat(result).isEqualTo(0);
            verify(weaponMock).getDice();
            verifyNoInteractions(mockRandom);
        }
    }

}
