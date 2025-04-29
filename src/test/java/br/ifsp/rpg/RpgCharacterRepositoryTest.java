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

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RpgCharacterRepositoryTest {

    @Mock CharacterRepository repository;
    @InjectMocks CharacterService service;

    RpgCharacter character;

    @BeforeEach
    void setUp() {
        Race human = new Race("Human", 5, 2, 2, 2);
        Weapon dagger = new Weapon("Dagger", 4, 3);
        character = new RpgCharacter("Character", ClassType.DUELIST, human, dagger);
    }

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Save character test")
    void saveCharacterTest(){
        when(repository.findById(Mockito.any())).thenReturn(Optional.of(character));

        service.save(character);

        assertThat(character.getId()).isNotNull();
        assertThat(service.getCharacter(character.getId())).isEqualTo(Optional.of(character));
    }

    @Test
    @Tag("Unit Test")
    @DisplayName("Trying to save invalid character test")
    void tryingToSaveInvalidCharacterTest(){
        Race human = new Race("Human", 5, 2, 2, 2);
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> service.save(new RpgCharacter(
                        "New Character", ClassType.BERSERK, human,null)));
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

}
