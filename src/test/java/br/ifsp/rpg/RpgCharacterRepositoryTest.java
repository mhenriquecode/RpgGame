package br.ifsp.rpg;

import br.ifsp.rpg.model.ClassType;
import br.ifsp.rpg.model.Race;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.model.Weapon;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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

        CharacterRepository repository = new CharacterRepository();
        repository.save(character);

        assertThat(character.getId()).isNotNull();
        assertThat(repository.findById(character.getId())).isEqualTo(character);
    }
}
