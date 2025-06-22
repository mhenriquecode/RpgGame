package br.ifsp.rpg.persistence;

import br.ifsp.rpg.integration.BaseApiIntegrationTest;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.repository.CharacterRepository;
import br.ifsp.web.repository.RpgCharacterEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(
        classes = br.ifsp.web.DemoAuthAppApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("PersistenceTest")
@Tag("IntegrationTest")
class CharacterRepositoryTest extends BaseApiIntegrationTest {

    @Autowired
    private CharacterRepository repository;

    private RpgCharacterEntity createAndPersist(String name, ClassType classType, Race race) {
        RpgCharacterEntity entity = new RpgCharacterEntity(name, classType, race, Weapon.SWORD);
        entity.setId(UUID.randomUUID());
        return repository.save(entity);
    }

    @Test
    @DisplayName("Deve contar personagens por classe corretamente")
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    void shouldCountCharactersByClass() {
        RpgCharacterEntity warrior1 = new RpgCharacterEntity("Warrior1", ClassType.WARRIOR, Race.HUMAN, Weapon.SWORD);
        warrior1.setId(UUID.randomUUID());
        RpgCharacterEntity warrior2 = new RpgCharacterEntity("Warrior2", ClassType.WARRIOR, Race.ORC, Weapon.AXE);
        warrior2.setId(UUID.randomUUID());
        RpgCharacterEntity berserker = new RpgCharacterEntity("Berserker", ClassType.BERSERK, Race.DWARF, Weapon.HAMMER);
        berserker.setId(UUID.randomUUID());

        repository.save(warrior1);
        repository.save(warrior2);
        repository.save(berserker);
        repository.flush();

        Map<ClassType, Long> contagem = repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        RpgCharacterEntity::getClassType,
                        Collectors.counting()
                ));

        assertEquals(2, contagem.get(ClassType.WARRIOR));
        assertEquals(1, contagem.get(ClassType.BERSERK));
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve falhar ao salvar personagem sem nome")
    void shouldFailWhenSavingCharacterWithoutName() {
        RpgCharacterEntity entity = new RpgCharacterEntity(
                null,
                ClassType.WARRIOR,
                Race.HUMAN,
                Weapon.SWORD
        );
        entity.setId(UUID.randomUUID());

        assertThrows(JpaSystemException.class, () -> {
            repository.save(entity);
            repository.flush();
        });
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve contar personagens por raça")
    void shouldCountCharactersByRace() {
        createAndPersist("A", ClassType.WARRIOR, Race.HUMAN);
        createAndPersist("B", ClassType.BERSERK, Race.ORC);
        createAndPersist("C", ClassType.PALADIN, Race.HUMAN);
        repository.flush();

        Map<Race, Long> countByRace = repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        RpgCharacterEntity::getRace,
                        Collectors.counting()
                ));

        assertEquals(2, countByRace.get(Race.HUMAN));
        assertEquals(1, countByRace.get(Race.ORC));
    }
    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve deletar personagem existente por ID")
    void shouldDeleteCharacterById() {
        RpgCharacterEntity entity = createAndPersist("Temp", ClassType.PALADIN, Race.HUMAN);
        UUID id = entity.getId();

        repository.deleteById(id);
        repository.flush();

        assertFalse(repository.existsById(id));
    }
}
