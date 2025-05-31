package br.ifsp.rpg.model;

import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.actions.AttackAction;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.model.specialEffects.SpecialEffectBerserk;
import br.ifsp.web.model.specialEffects.SpecialEffectPaladin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RpgCharacterTest {
    private RpgCharacter player1;
    @Mock private Random mockRandom = mock(Random.class);

    @BeforeEach
    void setUp() {
        player1 = new RpgCharacter("Character1", ClassType.PALADIN, Race.HUMAN, Weapon.SWORD);
    }
    private void assertCharacter(String name, ClassType classType, Race race) {
        RpgCharacter character = new RpgCharacter(name, classType, race, Weapon.SWORD);
        assertThat(character.getClassType()).isEqualTo(classType);
        assertThat(character.getRace()).isEqualTo(race);
        assertThat(character.getName()).isEqualTo(name);
    }

    @Nested
    @DisplayName("TDD Characters test")
    class TddCharactersTest {
        @Test
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Creating valid character test")
        void creatingValidCharacterTest() {
            RpgCharacter character = new RpgCharacter("Character", ClassType.WARRIOR, Race.ORC, Weapon.AXE);

            assertThat(character.getName()).isEqualTo("Character");
            assertThat(character.getRace().name()).isEqualTo(Race.ORC.name());
            assertThat(character.getWeapon().name()).isEqualTo(Weapon.AXE.name());
            assertThat(character.getClassType()).isEqualTo(ClassType.WARRIOR);
        }

        @Test
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Calculate character attributes test")
        void calculateCharacterAttributesTest(){
            RpgCharacter character = new RpgCharacter("Character", ClassType.PALADIN, Race.DWARF, Weapon.SWORD);

            assertThat(character.getMaxHealth()).isEqualTo(130);
            assertThat(character.getStrength()).isEqualTo(10);
            assertThat(character.getDefense()).isEqualTo(15);
            assertThat(character.getSpeed()).isEqualTo(4);
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

            assertThat(resultSpeed).isEqualTo(6);
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
        @DisplayName("Applying paladin special attack effect when health points is full test")
        void applyingPaladinSpecialAttackEffectWhenHealthIsFullTest() {
            when(mockRandom.nextInt(100)).thenReturn(5);

            RpgCharacter player = new RpgCharacter("Character", ClassType.PALADIN, Race.HUMAN, Weapon.SWORD, mockRandom);
            player.attack();

            assertThat(player.getClassType()).isEqualTo(ClassType.PALADIN);
            assertThat(player.getHealth()).isEqualTo(115);
        }

        @Test
        @Tag("Unit-test")
        @Tag("TDD")
        @DisplayName("Applying paladin special attack effect when health points is not full test")
        void applyingPaladinSpecialAttackEffectWhenHealthIsNotFullTest() {
            when(mockRandom.nextInt(100)).thenReturn(5);

            RpgCharacter player = new RpgCharacter("Character", ClassType.PALADIN, Race.HUMAN, Weapon.SWORD, mockRandom);
            player.setHealth(80);
            player.attack();

            assertThat(player.getClassType()).isEqualTo(ClassType.PALADIN);
            assertThat(player.getHealth()).isBetween(93, 103);
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

    @Nested
    @DisplayName("Unit Characters test")
    class UnitCharactersTest {
        @Test
        @Tag("Unit-test")
        @DisplayName("User Chooses Elf Then Character Gets Speed Bonus")
        void userChoosesElfThenCharacterGetsSpeedBonus() {
            RpgCharacter character = new RpgCharacter("Legolas", ClassType.WARRIOR, Race.ELF, Weapon.SWORD);
            int totalSpeed = 4 + Race.ELF.getBonusSpeed() + ClassType.WARRIOR.getBonusSpeed(); // 4 + 2 + 0

            assertEquals(totalSpeed, character.getSpeed());
        }

        @Test
        @Tag("Unit-test")
        @DisplayName("User Chooses Dwarf Then Character Receives Life And Defense Bonus")
        void userChoosesDwarfThenCharacterReceivesLifeAndDefenseBonus() {
            RpgCharacter character = new RpgCharacter("Gimli", ClassType.PALADIN, Race.DWARF, Weapon.AXE);

            int totalLife = 100 + Race.DWARF.getBonusHealth() + ClassType.PALADIN.getBonusHealth(); // 100 + 20 + 10
            int totalDefense = 5 + Race.DWARF.getBonusDefense() + ClassType.PALADIN.getBonusDefense(); // 5 + 5 + 5

            assertEquals(totalLife, character.getMaxHealth());
            assertEquals(totalDefense, character.getDefense());
        }

        @Test
        @Tag("Unit-test")
        @DisplayName("User Chooses Human Then Character Receives Moderate Bonus To All Attributes")
        void userChoosesHumanThenCharacterReceivesModerateBonusToAllAttributes() {
            RpgCharacter character = new RpgCharacter("Aragorn", ClassType.BERSERK, Race.HUMAN, Weapon.SWORD);
            int expectedHealth = 100 + Race.HUMAN.getBonusHealth() + ClassType.BERSERK.getBonusHealth(); // 100 + 5 + 20
            int expectedStrength = 10 + Race.HUMAN.getBonusStrength() + ClassType.BERSERK.getBonusStrength(); // 10 + 2 + 5
            int expectedDefense = 5 + Race.HUMAN.getBonusDefense() + ClassType.BERSERK.getBonusDefense(); // 5 + 2 + 0
            int expectedSpeed = 4 + Race.HUMAN.getBonusSpeed() + ClassType.BERSERK.getBonusSpeed(); // 4 + 2 + 0

            assertEquals(expectedHealth, character.getMaxHealth());
            assertEquals(expectedStrength, character.getStrength());
            assertEquals(expectedDefense, character.getDefense());
            assertEquals(expectedSpeed, character.getSpeed());
        }

        @Test
        @Tag("Unit-test")
        @DisplayName("User Chooses Orc Then Character Receives Bonus To Strength")
        void userChoosesOrcThenCharacterReceivesBonusToStrength() {
            RpgCharacter character = new RpgCharacter("Gor", ClassType.DUELIST, Race.ORC, Weapon.AXE);
            int expectedStrength = 10 + Race.ORC.getBonusStrength() + ClassType.DUELIST.getBonusStrength(); // 10 + 5 + 5

            assertEquals(expectedStrength, character.getStrength());
        }

        @Test
        @Tag("Unit-test")
        @DisplayName("User Chooses Berserk Then Character Receives Bonus To Health And Strength")
        void userChoosesBerserkThenCharacterReceivesBonusToHealthAndStrength() {
            RpgCharacter character = new RpgCharacter("Thorg", ClassType.BERSERK, Race.HUMAN, Weapon.HAMMER);

            int expectedMaxHealth = 100 + Race.HUMAN.getBonusHealth() + ClassType.BERSERK.getBonusHealth(); // 100 + 5 + 20
            int expectedStrength = 10 + Race.HUMAN.getBonusStrength() + ClassType.BERSERK.getBonusStrength(); // 10 + 2 + 5

            assertEquals(expectedMaxHealth, character.getMaxHealth());
            assertEquals(expectedStrength, character.getStrength());
        }

        @Test
        @Tag("Unit-test")
        @DisplayName("User Chooses Warrior Then Character Receives Bonus To Strength And Defense")
        void userChoosesWarriorThenCharacterReceivesBonusToStrengthAndDefense() {
            RpgCharacter character = new RpgCharacter("Conan", ClassType.WARRIOR, Race.HUMAN, Weapon.SWORD);

            int expectedStrength = 10 + Race.HUMAN.getBonusStrength() + ClassType.WARRIOR.getBonusStrength(); // 10 + 2 + 5
            int expectedDefense = 5 + Race.HUMAN.getBonusDefense() + ClassType.WARRIOR.getBonusDefense(); // 5 + 2 + 5

            assertEquals(expectedStrength, character.getStrength());
            assertEquals(expectedDefense, character.getDefense());
        }

        @Test
        @Tag("Unit-test")
        @DisplayName("User Chooses Duelist Then Character Receives Bonus To Strength And Speed")
        void userChoosesDuelistThenCharacterReceivesBonusToStrengthAndSpeed() {
            RpgCharacter character = new RpgCharacter("Zoro", ClassType.DUELIST, Race.HUMAN, Weapon.SWORD);

            int expectedStrength = 10 + Race.HUMAN.getBonusStrength() + ClassType.DUELIST.getBonusStrength(); // 10 + 2 + 5
            int expectedSpeed = 4 + Race.HUMAN.getBonusSpeed() + ClassType.DUELIST.getBonusSpeed(); // 4 + 2 + 2

            assertEquals(expectedStrength, character.getStrength());
            assertEquals(expectedSpeed, character.getSpeed());
        }

        @Test
        @Tag("Unit-test")
        @DisplayName("User Chooses Paladin Then Character Receives Bonus To Health And Defense")
        void userChoosesPaladinThenCharacterReceivesBonusToHealthAndDefense() {
            RpgCharacter character = new RpgCharacter("Arthur", ClassType.PALADIN, Race.HUMAN, Weapon.SWORD);

            int expectedMaxHealth = 100 + Race.HUMAN.getBonusHealth() + ClassType.PALADIN.getBonusHealth(); // 100 + 5 + 10
            int expectedDefense = 5 + Race.HUMAN.getBonusDefense() + ClassType.PALADIN.getBonusDefense(); // 5 + 2 + 5

            assertEquals(expectedMaxHealth, character.getMaxHealth());
            assertEquals(expectedDefense, character.getDefense());
        }
        @Nested
        @DisplayName("Struct Character tests")
        class StructCharacterTest {
            @Test
            @Tag("Unit-test")
            @Tag("Structural")
            @DisplayName("Set Name Throws Exception When Name Is Null")
            void setNameThrowsExceptionWhenNameIsNull() {
                RpgCharacter character = new RpgCharacter("Test", ClassType.WARRIOR, Race.HUMAN, Weapon.SWORD);

                assertThatThrownBy(() -> character.setName(null))
                        .isInstanceOf(NullPointerException.class)
                        .hasMessage("Name cannot be null");
            }
            @Test
            @Tag("Unit-test")
            @Tag("Structural")
            @DisplayName("Set Name Should Update Name")
            void setNameShouldUpdateName() {
                RpgCharacter character = new RpgCharacter("OldName", ClassType.WARRIOR, Race.HUMAN, Weapon.SWORD);
                character.setName("NewName");
                assertThat(character.getName()).isEqualTo("NewName");
            }
        }

        @Nested
        @DisplayName("Mutation Character test")
        public class mutationCharacterTest {
            @Test
            @Tag("Unit-test")
            @Tag("Mutation")
            @DisplayName("Paladin Effect Should Return Original Damage")
            void paladinEffectShouldReturnOriginalDamage() {
                RpgCharacter character = mock(RpgCharacter.class);

                when(character.getHealth()).thenReturn(60);
                when(character.getMaxHealth()).thenReturn(100);

                SpecialEffectPaladin effect = new SpecialEffectPaladin();
                int result = effect.applyEffect(character, 30);

                assertThat(30).isEqualTo(result);
            }
        }
    }
}
