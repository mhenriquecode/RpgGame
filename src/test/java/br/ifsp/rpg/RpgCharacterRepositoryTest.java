package br.ifsp.rpg;

import br.ifsp.rpg.interfaces.CharacterRepository;
import br.ifsp.rpg.model.ClassType;
import br.ifsp.rpg.model.Race;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.model.Weapon;
import br.ifsp.rpg.repository.MemoryCharacterRepository;
import br.ifsp.rpg.service.CharacterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RpgCharacterRepositoryTest {

    @Mock CharacterRepository repository;
    @InjectMocks CharacterService service;

    Weapon dagger;
    RpgCharacter character;

    @BeforeEach
    void setUp() {
        dagger = new Weapon("Dagger", 4, 3);
        character = new RpgCharacter("Character", ClassType.DUELIST, Race.HUMAN, dagger);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Find character by id test")
    void findCharacterByIdTest(){
        when(repository.findById(character.getId())).thenReturn(Optional.of(character));
        assertThat(service.getCharacter(character.getId())).isEqualTo(Optional.of(character));
    }

    @Test
    @Tag("Unit Test")
    @DisplayName("Trying to find character that does not exists test")
    void findCharacterThatDoesNotExistTest(){
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.getCharacter(UUID.randomUUID()));
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Get all characters test")
    void getAllCharactersTest(){
        Weapon hammer = new Weapon("Hammer", 1, 12);

        RpgCharacter character2 = new RpgCharacter("Character2", ClassType.WARRIOR, Race.DWARF, hammer);
        List<RpgCharacter> mockList = List.of(character, character2);

        when(repository.findAll()).thenReturn(mockList);

        List<RpgCharacter> result = service.getAllCharacters();
        assertThat(result).hasSize(2).containsExactlyInAnyOrder(character, character2);
    }

    @Test
    @Tag("Unit Test")
    @DisplayName("Return empty list when repository is empty")
    void getAllCharactersEmptyTest(){
        assertThat(service.getAllCharacters()).isEmpty();
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Save character test")
    void saveCharacterTest(){
        when(repository.findById(Mockito.any())).thenReturn(Optional.empty());

        service.save(character);
        assertThat(character.getId()).isNotNull();

        when(repository.findById(character.getId())).thenReturn(Optional.of(character));
        assertThat(service.getCharacter(character.getId())).isEqualTo(Optional.of(character));
    }

    @Test
    @Tag("Unit Test")
    @DisplayName("Trying to save invalid weapon character test")
    void tryingToSaveInvalidWeaponCharacterTest(){
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.save(new RpgCharacter(
                        "New Character", ClassType.BERSERK, Race.HUMAN,null)));
    }

    @Test
    @Tag("Unit Test")
    @DisplayName("Trying to save invalid race character test")
    void tryingToSaveInvalidRaceCharacterTest(){
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.save(new RpgCharacter(
                        "New Character", ClassType.BERSERK, null,dagger)));
    }

    @Test
    @Tag("Unit Test")
    @DisplayName("Trying to save invalid name character test")
    void tryingToSaveInvalidNameCharacterTest(){
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.save(new RpgCharacter(
                        null, ClassType.BERSERK, Race.HUMAN,dagger)));
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Update character test")
    void updateCharacterTest(){
        when(repository.findById(character.getId())).thenReturn(Optional.of(character));

        Weapon newWeapon = new Weapon("Sword", 3, 4);
        service.update(character.getId(), "New Name", ClassType.PALADIN, Race.ORC, newWeapon);

        assertThat(character.getName()).isEqualTo("New Name");
        assertThat(character.getClassType()).isEqualTo(ClassType.PALADIN);
        assertThat(character.getRace()).isEqualTo(Race.ORC);
        assertThat(character.getWeapon()).isEqualTo(newWeapon);

        verify(repository).update(character);
    }

    @Test
    @Tag("Unit Test")
    @DisplayName("Trying to update non existing character")
    void tryingToUpdateNonExistingCharacterTest(){
        Weapon newWeapon = new Weapon("Hammer", 1, 12);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.update(UUID.randomUUID(), "New Name", ClassType.WARRIOR, Race.ORC, newWeapon));
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Delete existing character test")
    void deleteExistingCharacterTest(){
        when(repository.findById(Mockito.any())).thenReturn(Optional.of(character));
        service.delete(character.getId());
        verify(repository).delete(character.getId());
    }

    @Test
    @Tag("Unit Test")
    @DisplayName("Delete non existing character test")
    void deleteNonExistingCharacterTest(){
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.delete(UUID.randomUUID()));
    }
}
