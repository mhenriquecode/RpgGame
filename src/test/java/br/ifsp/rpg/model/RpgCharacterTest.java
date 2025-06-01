package br.ifsp.rpg.model;

import br.ifsp.web.interfaces.SpecialEffect;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.actions.AttackAction;
import br.ifsp.web.model.dice.RollAttackDice;
import br.ifsp.web.model.dice.RollHitDice;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.model.specialEffects.SpecialEffectBerserk;
import br.ifsp.web.model.specialEffects.SpecialEffectPaladin;
import br.ifsp.web.model.specialEffects.SpecialEffectWarrior;
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
            assertThat(player.getHealth()).isBetween(93, 105);
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
            @Test
            @Tag("Unit-test")
            @Tag("Structural")
            @DisplayName("Should Setting The Same Attributes")
            void shouldSettingTheSameAttributes() {
                ClassType classType = ClassType.WARRIOR;
                Race race = Race.HUMAN;
                Weapon weapon = Weapon.SWORD;

                RpgCharacter character = new RpgCharacter("Hero", classType, race, weapon);

                character.setMaxHealth(150);
                character.setHealth(120);
                character.setStrength(25);
                character.setDefense(10);
                character.setSpeed(18);
                character.setArmor(5);
                character.setDefending(true);

                assertThat(character.getId()).isNotNull();
                assertThat(character.getClassType()).isEqualTo(classType);
                assertThat(character.getRace()).isEqualTo(race);
                assertThat(character.getWeapon()).isEqualTo(weapon);

                assertThat(character.getMaxHealth()).isEqualTo(150);
                assertThat(character.getHealth()).isEqualTo(120);
                assertThat(character.getStrength()).isEqualTo(25);
                assertThat(character.getDefense()).isEqualTo(10);
                assertThat(character.getSpeed()).isEqualTo(18);
                assertThat(character.getArmor()).isEqualTo(5);
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
            @Test
            @Tag("Unit-test")
            @Tag("Mutation")
            @DisplayName("Paladin SetHealth To Max When Health Plus Damage Exceeds Max Health")
            void shouldSetHealthToMaxWhenHealthPlusDamageExceedsMaxHealth() {
                RpgCharacter character = mock(RpgCharacter.class);

                when(character.getHealth()).thenReturn(90);
                when(character.getMaxHealth()).thenReturn(100);

                SpecialEffectPaladin effect = new SpecialEffectPaladin();
                effect.applyEffect(character, 15);
                verify(character).setHealth(100);
            }
            @Test
            @Tag("Unit-test")
            @Tag("Mutation")
            @DisplayName("Paladin Should Set Health To Health Plus Damage When Equal To MaxHealth")
            void shouldSetHealthToHealthPlusDamageWhenEqualToMaxHealth() {
                RpgCharacter character = mock(RpgCharacter.class);

                when(character.getHealth()).thenReturn(80);
                when(character.getMaxHealth()).thenReturn(100);

                SpecialEffectPaladin effect = new SpecialEffectPaladin();
                effect.applyEffect(character, 20);

                verify(character).setHealth(100);
            }
            @Test
            @Tag("Unit-test")
            @Tag("Mutation")
            @DisplayName("Clone Should Copy Attributes Correctly")
            void cloneShouldCopyAttributesCorrectly() {
                RpgCharacter original = new RpgCharacter("Hero", ClassType.BERSERK, Race.ELF, Weapon.SWORD);
                original.setHealth(80);
                original.setStrength(20);
                original.setSpeed(10);
                original.setDefense(8);
                original.setArmor(15);

                RpgCharacter clone = original.cloneForCombat();

                assertThat(clone.getHealth()).isEqualTo(80);
                assertThat(clone.getStrength()).isEqualTo(20);
                assertThat(clone.getSpeed()).isEqualTo(10);
                assertThat(clone.getDefense()).isEqualTo(8);
                assertThat(clone.getArmor()).isEqualTo(15);
            }
            @Test
            @Tag("Unit-test")
            @Tag("Mutation")
            @DisplayName("Roll Attack Dice In RpgCharacter Should Use Injected Dice")
            void rollAttackDiceShouldUseInjectedDice() {
                RollAttackDice dice = mock(RollAttackDice.class);
                when(dice.roll()).thenReturn(7);

                RpgCharacter character = new RpgCharacter("Hero", ClassType.BERSERK, Race.ELF, Weapon.SWORD, new RollHitDice(), dice);

                assertThat(character.rollAttackDice()).isEqualTo(7);
            }
            @Test
            @Tag("Unit-test")
            @Tag("Mutation")
            @DisplayName("should apply special effect when random is 9 (chance = 10%)")
            void shouldApplySpecialEffectWhenChanceSucceeds() {
                RollAttackDice attackDice = mock(RollAttackDice.class);
                when(attackDice.roll()).thenReturn(5);
                RollHitDice hitDice = mock(RollHitDice.class);

                RpgCharacter character = new RpgCharacter("Hero", ClassType.BERSERK, Race.ELF, Weapon.SWORD, hitDice, attackDice);
                character.setStrength(10);

                SpecialEffect effect = mock(SpecialEffect.class);
                when(effect.applyEffect(any(), eq(15))).thenReturn(999); // 10 + 5 = 15 original
                character.setSpecialEffect(effect);

                Random fixedRandom = mock(Random.class);
                when(fixedRandom.nextInt(100)).thenReturn(9);
                character.setRandom(fixedRandom);
                int attack = character.attack();

                assertThat(attack).isEqualTo(999);
                verify(effect).applyEffect(character, 15);
            }
            @Test
            @Tag("Unit-test")
            @Tag("Mutation")
            @DisplayName("should not apply special effect when random is 10 (chance = 11%)")
            void shouldNotApplySpecialEffectWhenChanceFails() {
                RollAttackDice attackDice = mock(RollAttackDice.class);
                when(attackDice.roll()).thenReturn(5);

                RollHitDice hitDice = mock(RollHitDice.class);

                RpgCharacter character = new RpgCharacter("Hero", ClassType.BERSERK, Race.ELF, Weapon.SWORD, hitDice, attackDice);
                character.setStrength(10);

                SpecialEffect effect = mock(SpecialEffect.class);
                character.setSpecialEffect(effect);

                Random fixedRandom = mock(Random.class);
                when(fixedRandom.nextInt(100)).thenReturn(10);
                character.setRandom(fixedRandom);

                int attack = character.attack();

                assertThat(attack).isEqualTo(15); // 10 + 5 = 15
                verify(effect, never()).applyEffect(any(), anyInt());
            }
            @Test
            @Tag("Unit-test")
            @Tag("Mutation")
            @DisplayName("should initialize attributes correctly via all constructors")
            void shouldInitializeAttributesViaAllConstructors() {
                var hitDice = mock(RollHitDice.class);
                var attackDice = mock(RollAttackDice.class);
                var random = mock(Random.class);

                var c1 = new RpgCharacter("Alice", ClassType.BERSERK, Race.HUMAN, Weapon.AXE, hitDice, attackDice);
                var c2 = new RpgCharacter("Bob", ClassType.WARRIOR, Race.ORC, Weapon.SWORD, random);

                assertThat(c1.getHealth()).isEqualTo(100 + 5 + 20);     // 125
                assertThat(c1.getStrength()).isEqualTo(10 + 2 + 5);     // 17
                assertThat(c1.getDefense()).isEqualTo(5 + 2 + 0);       // 7
                assertThat(c1.getSpeed()).isEqualTo(4 + 2 + 0);         // 6
                assertThat(c1.getArmor()).isEqualTo(10);                // base

                assertThat(c2.getHealth()).isEqualTo(100 + 0 + 0);      // 100
                assertThat(c2.getStrength()).isEqualTo(10 + 5 + 5);     // 20
                assertThat(c2.getDefense()).isEqualTo(5 + 0 + 5);       // 10
                assertThat(c2.getSpeed()).isEqualTo(4 + 0 + 0);         // 4
                assertThat(c2.getArmor()).isEqualTo(10);                // base
            }


        }
    }
}
