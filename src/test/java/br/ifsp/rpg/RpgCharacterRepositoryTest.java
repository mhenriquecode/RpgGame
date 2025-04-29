package br.ifsp.rpg;

import br.ifsp.rpg.interfaces.CharacterRepository;
import br.ifsp.rpg.model.ClassType;
import br.ifsp.rpg.model.Race;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.model.Weapon;
import br.ifsp.rpg.repository.MemoryCharacterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class RpgCharacterRepositoryTest {

    @Test
    @Tag("Unit Test")
    @Tag("TDD")
    @DisplayName("Save character test")
    void saveCharacterTest(){
        Race human = new Race("Human", 5, 2, 2, 2);
        Weapon dagger = new Weapon("Dagger", 4, 3);
        ClassType duelist = ClassType.DUELIST;
        RpgCharacter character = new RpgCharacter("Character", duelist, human, dagger);

        MemoryCharacterRepository repository = new MemoryCharacterRepository();
        repository.save(character);

        assertThat(character.getId()).isNotNull();
        assertThat(repository.findById(character.getId())).isEqualTo(Optional.of(character));
    }
}
