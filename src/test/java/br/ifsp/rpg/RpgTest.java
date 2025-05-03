package br.ifsp.rpg;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import br.ifsp.rpg.model.*;
import br.ifsp.rpg.model.enums.ClassType;
import br.ifsp.rpg.model.enums.Race;
import br.ifsp.rpg.model.enums.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Random;

public class RpgTest {

    @Mock private Random mockRandom = mock(Random.class);

    @BeforeEach
    public void setUp() {
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Applying berserk special attack effect test")
    void applyingBerserkSpecialAttackEffectTest(){
        when(mockRandom.nextInt(100)).thenReturn(5);

        RpgCharacter player = new RpgCharacter("Character", ClassType.BERSERK, Race.ORC, Weapon.AXE, mockRandom);

        assertThat(player.getClassType()).isEqualTo(ClassType.BERSERK);
        assertThat(player.attack()).isGreaterThanOrEqualTo(player.getStrength() + 2 * 2);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Applying warrior special attack effect test")
    void applyingWarriorSpecialAttackEffectTest(){
        when(mockRandom.nextInt(100)).thenReturn(5);

        RpgCharacter player = new RpgCharacter("Character", ClassType.WARRIOR, Race.HUMAN, Weapon.SWORD, mockRandom);
        int oldDefense = player.getDefense();
        int damage = player.attack();

        assertThat(player.getClassType()).isEqualTo(ClassType.WARRIOR);
        assertThat(player.getDefense()).isEqualTo(oldDefense + damage);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Applying paladin special attack effect test")
    void applyingPaladinSpecialAttackEffectTest() {
        when(mockRandom.nextInt(100)).thenReturn(5);

        RpgCharacter player = new RpgCharacter("Character", ClassType.PALADIN, Race.HUMAN, Weapon.SWORD, mockRandom);
        player.attack();

        assertThat(player.getClassType()).isEqualTo(ClassType.PALADIN);
        assertThat(player.getHealth()).isGreaterThanOrEqualTo(118);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Applying duelist special attack effect test")
    void applyingDuelistSpecialAttackEffectTest() {
        when(mockRandom.nextInt(100)).thenReturn(5);

        RpgCharacter player = new RpgCharacter("Character", ClassType.DUELIST, Race.HUMAN, Weapon.SWORD, mockRandom);

        assertThat(player.getClassType()).isEqualTo(ClassType.DUELIST);
        assertThat(player.attack()).isStrictlyBetween(23, 41);
    }
}
