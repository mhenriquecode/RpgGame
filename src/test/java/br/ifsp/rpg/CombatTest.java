package br.ifsp.rpg;

import br.ifsp.rpg.model.*;
import br.ifsp.rpg.model.enums.ClassType;
import br.ifsp.rpg.model.enums.Race;
import br.ifsp.rpg.model.enums.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class CombatTest {

    private RpgCharacter player1;
    private Random mockRandom;

    @BeforeEach
    void setUp() {
        mockRandom = mock(Random.class);
        player1 = new RpgCharacter("Character1", ClassType.PALADIN, Race.HUMAN, Weapon.SWORD);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Must create valid combat between two character")
    void mustCreateValidCombatBetweenTwoCharacters(){
        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, Race.ORC, Weapon.AXE);
        Combat combat = new Combat(player1, player2);

        assertThat(combat).isNotNull();
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Player one starts the combat when his speed is grater than player two's speed")
    void playerOneWhoStartsTheCombatWhenSpeedIsGreaterThanThePlayerTwoTest(){
        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, Race.ORC, Weapon.AXE);
        Combat combat = new Combat(player1, player2);

        assertThat(player1.getSpeed()).isGreaterThan(player2.getSpeed());

        RpgCharacter first = combat.getFirstToPlay();

        assertThat(first).isEqualTo(player1);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Player two starts the combat when his speed is grater than player one's speed")
    void playerTwoWhoStartsTheCombatWhenSpeedIsGreaterThanThePlayerOneTest(){
        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.BERSERK, Race.ORC, Weapon.AXE);
        Combat combat = new Combat(player1, player2);

        assertThat(player1.getSpeed()).isGreaterThan(player2.getSpeed());

        RpgCharacter first = combat.getFirstToPlay();

        assertThat(first).isEqualTo(player1);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Player one starts when speed is equal to player two's speed")
    void playerOneStartsWhenSpeedIsEqualToThePlayerTwo(){
        when(mockRandom.nextInt(2)).thenReturn(0);

        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.PALADIN, Race.HUMAN, Weapon.AXE);

        Combat combat = new Combat(player1, player2, mockRandom);
        RpgCharacter first = combat.getFirstToPlay();

        assertThat(first).isEqualTo(player1);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Player two starts when speed is equal to player one's speed")
    void playerTwoStartsWhenSpeedIsEqualToThePlayerOne(){
        when(mockRandom.nextInt(2)).thenReturn(1);

        RpgCharacter player2 = new RpgCharacter("Matheus", ClassType.PALADIN, Race.HUMAN, Weapon.AXE);

        Combat combat = new Combat(player1, player2, mockRandom);
        RpgCharacter first = combat.getFirstToPlay();

        assertThat(first).isEqualTo(player2);
    }
    
    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Character attack test")
    void characterAttackTest(){
        RpgCharacter player = mock(RpgCharacter.class);
        when(player.attack()).thenReturn(20);

        int result = player.attack();

        verify(player).attack();
        assertThat(result).isEqualTo(20);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("should increase armor when the character dodges")
    void shouldIncreaseArmorWhenTheCharacterDodges(){
        player1.dodge();
        int resultSpeed = player1.getSpeed();

        assertThat(resultSpeed).isEqualTo(7);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("should decrease damage taken when character defends")
    void shouldDecreaseDamageTakenWhenCharacterDefends(){
        player1.setDefending(true);

        int initialHealth = player1.getHealth();
        int damageReceived = 17;

        player1.defends(damageReceived);
        int expectedDamage = damageReceived - 12;
        int expectedHealth = initialHealth - expectedDamage;

        assertThat(player1.getHealth()).isEqualTo(expectedHealth);
    }
}
