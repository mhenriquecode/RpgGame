package br.ifsp.rpg;

import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.exception.CharacterNotFoundException;
import br.ifsp.web.repository.CharacterRepository;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.service.CharacterService;
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

    RpgCharacter character;

    @BeforeEach
    void setUp() {
        character = new RpgCharacter("Character", ClassType.DUELIST, Race.HUMAN, Weapon.DAGGER);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Find character by id test")
    void findCharacterByIdTest(){
        when(repository.findById(character.getId())).thenReturn(Optional.of(character));
        assertThat(service.getCharacter(character.getId())).isEqualTo(Optional.of(character));
    }

    @Test
    @Tag("Unit-test")
    @DisplayName("Trying to find character that does not exists test")
    void findCharacterThatDoesNotExistTest(){
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.getCharacter(UUID.randomUUID()));
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Get all characters test")
    void getAllCharactersTest(){
        RpgCharacter character2 = new RpgCharacter("Character2", ClassType.WARRIOR, Race.DWARF, Weapon.HAMMER);
        List<RpgCharacter> mockList = List.of(character, character2);

        when(repository.findAll()).thenReturn(mockList);

        List<RpgCharacter> result = service.getAllCharacters();
        assertThat(result).hasSize(2).containsExactlyInAnyOrder(character, character2);
    }

    @Test
    @Tag("Unit-test")
    @DisplayName("Return empty list when repository is empty")
    void getAllCharactersEmptyTest(){
        assertThat(service.getAllCharacters()).isEmpty();
    }

    @Test
    @Tag("Unit-test")
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
    @Tag("Unit-test")
    @DisplayName("Trying to save invalid weapon character test")
    void tryingToSaveInvalidWeaponCharacterTest(){
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.save(new RpgCharacter(
                        "New Character", ClassType.BERSERK, Race.HUMAN,null)));
    }

    @Test
    @Tag("Unit-test")
    @DisplayName("Trying to save invalid race character test")
    void tryingToSaveInvalidRaceCharacterTest(){
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.save(new RpgCharacter(
                        "New Character", ClassType.BERSERK, null,Weapon.DAGGER)));
    }

    @Test
    @Tag("Unit-test")
    @DisplayName("Trying to save invalid class type character test")
    void tryingToSaveInvalidClassTypeCharacterTest(){
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.save(new RpgCharacter(
                        "New Character", null, Race.HUMAN,Weapon.HAMMER)));
    }

    @Test
    @Tag("Unit-test")
    @DisplayName("Trying to save invalid name character test")
    void tryingToSaveInvalidNameCharacterTest(){
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.save(new RpgCharacter(
                        null, ClassType.BERSERK, Race.HUMAN,Weapon.DAGGER)));
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Update character test")
    void updateCharacterTest(){
        when(repository.findById(character.getId())).thenReturn(Optional.of(character));

        service.update(character.getId(), new CharacterDTO("New Name", ClassType.PALADIN, Race.ORC, Weapon.SWORD));

        assertThat(character.getName()).isEqualTo("New Name");
        assertThat(character.getClassType()).isEqualTo(ClassType.PALADIN);
        assertThat(character.getRace()).isEqualTo(Race.ORC);
        assertThat(character.getWeapon()).isEqualTo(Weapon.SWORD);

        verify(repository).save(character);
    }

    @Test
    @Tag("Unit-test")
    @DisplayName("Trying to update non existing character")
    void tryingToUpdateNonExistingCharacterTest(){
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.update(
                        UUID.randomUUID(),
                        new CharacterDTO("New Name", ClassType.WARRIOR, Race.ORC, Weapon.HAMMER)));
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Delete existing character test")
    void deleteExistingCharacterTest(){
        when(repository.findById(Mockito.any())).thenReturn(Optional.of(character));
        service.delete(character.getId());
        verify(repository).delete(character);
    }

    @Test
    @Tag("Unit-test")
    @DisplayName("Delete non existing character test")
    void deleteNonExistingCharacterTest(){
        assertThatExceptionOfType(CharacterNotFoundException.class)
                .isThrownBy(() -> service.delete(UUID.randomUUID()));
    }
}
