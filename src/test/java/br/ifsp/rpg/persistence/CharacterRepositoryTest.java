package br.ifsp.rpg.persistence;

import br.ifsp.rpg.integration.BaseApiIntegrationTest;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.repository.CharacterRepository;
import br.ifsp.web.repository.RpgCharacterEntity;
import org.hibernate.exception.DataException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaSystemException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = br.ifsp.web.DemoAuthAppApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("PersistenceTest")
@Tag("IntegrationTest")
class CharacterRepositoryTest extends BaseApiIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CharacterRepository repository;

    @BeforeEach
    void clearDatabase() {
        jdbcTemplate.execute("DELETE FROM characters");
    }

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

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve atualizar nome de personagem existente")
    void shouldUpdateCharacterName() {
        RpgCharacterEntity entity = createAndPersist("OldName", ClassType.WARRIOR, Race.ORC);
        UUID id = entity.getId();

        entity.setName("NewName");
        repository.save(entity);
        repository.flush();

        RpgCharacterEntity updated = repository.findById(id).orElseThrow();
        assertEquals("NewName", updated.getName());
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve calcular estatísticas de personagens por raça")
    void shouldComputeCharacterStatisticsByRace() {
        createAndPersist("C1", ClassType.WARRIOR, Race.HUMAN);
        createAndPersist("C2", ClassType.BERSERK, Race.ORC);
        createAndPersist("C3", ClassType.PALADIN, Race.HUMAN);
        repository.flush();

        Map<Race, Long> countByRace = repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        RpgCharacterEntity::getRace,
                        Collectors.counting()
                ));

        double avgCount = countByRace.values().stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0);

        long maxCount = countByRace.values().stream()
                .mapToLong(Long::longValue)
                .max()
                .orElse(0);

        assertEquals(1.5, avgCount);
        assertEquals(2, maxCount);
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve lidar com atualizações concorrentes sem perder dados")
    void shouldHandleConcurrentUpdatesWithoutDataLoss() throws InterruptedException {
        RpgCharacterEntity entity = createAndPersist("Concurrent", ClassType.WARRIOR, Race.HUMAN);
        UUID id = entity.getId();

        Runnable updater = () -> {
            RpgCharacterEntity e = repository.findById(id).orElseThrow();
            e.setName("Updated_" + Thread.currentThread().getId());
            repository.save(e);
        };

        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executor.execute(updater);
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        RpgCharacterEntity finalEntity = repository.findById(id).orElseThrow();
        assertNotNull(finalEntity.getName());
        assertTrue(finalEntity.getName().startsWith("Updated_"));
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve suportar inserção em massa de 10k registros")
    void shouldHandleBulkInsertOf10kRecords() {
        List<RpgCharacterEntity> entities = new ArrayList<>();

        for (int i = 0; i < 10_000; i++) {
            RpgCharacterEntity entity = new RpgCharacterEntity(
                    "Char_" + i,
                    ClassType.values()[i % ClassType.values().length],
                    Race.values()[i % Race.values().length],
                    Weapon.values()[i % Weapon.values().length]
            );
            entity.setId(UUID.randomUUID());
            entities.add(entity);
        }

        long startTime = System.currentTimeMillis();
        repository.saveAll(entities);
        repository.flush();
        long duration = System.currentTimeMillis() - startTime;

        assertEquals(10_000, repository.count());
        assertTrue(duration < 10_000, "Insertion took too long: " + duration + "ms");

        repository.deleteAll();
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve rejeitar valores extremos em campos")
    void shouldRejectExtremeFieldValues() {
        String longName = "A".repeat(300);
        RpgCharacterEntity entity = new RpgCharacterEntity(
                longName,
                ClassType.WARRIOR,
                Race.HUMAN,
                Weapon.SWORD
        );
        entity.setId(UUID.randomUUID());

        assertThrows(DataException.class, () -> {
            repository.save(entity);
            repository.flush();
        });
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve evitar deadlock em deleções concorrentes")
    void shouldPreventDeadlockOnConcurrentDeletes() throws InterruptedException {
        RpgCharacterEntity char1 = createAndPersist("Deadlock_1", ClassType.WARRIOR, Race.ORC);
        RpgCharacterEntity char2 = createAndPersist("Deadlock_2", ClassType.BERSERK, Race.ELF);

        Runnable thread1 = () -> {
            repository.deleteById(char1.getId());
            repository.deleteById(char2.getId());
        };

        Runnable thread2 = () -> {
            repository.deleteById(char2.getId());
            repository.deleteById(char1.getId());
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.execute(thread1);
        executor.execute(thread2);

        executor.shutdown();
        boolean completed = executor.awaitTermination(5, TimeUnit.SECONDS);

        assertTrue(completed, "Potential deadlock detected");
    }
}
