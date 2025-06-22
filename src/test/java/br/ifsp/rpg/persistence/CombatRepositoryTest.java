package br.ifsp.rpg.persistence;

import br.ifsp.rpg.integration.BaseApiIntegrationTest;
import br.ifsp.web.log.CombatLog;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.repository.CharacterRepository;
import br.ifsp.web.repository.CombatLogRepository;
import br.ifsp.web.repository.RpgCharacterEntity;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(
        classes = br.ifsp.web.DemoAuthAppApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CombatRepositoryTest extends BaseApiIntegrationTest {

    @Autowired
    private CombatLogRepository combatLogRepository;

    @Autowired
    private CharacterRepository characterRepository;

    private RpgCharacterEntity createAndPersistCharacter(String name, Race race) {
        RpgCharacterEntity character = new RpgCharacterEntity(name, ClassType.WARRIOR, race, Weapon.SWORD);
        character.setId(UUID.randomUUID());
        return characterRepository.saveAndFlush(character);
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve salvar e encontrar um log de combate com sucesso")
    void shouldSaveAndFindCombatLogSuccessfully() {
        RpgCharacterEntity player1 = createAndPersistCharacter("Alan", Race.HUMAN);
        RpgCharacterEntity player2 = createAndPersistCharacter("Ana", Race.ORC);

        CombatLog newLog = new CombatLog(player1, player2, player1);
        combatLogRepository.saveAndFlush(newLog);

        Optional<CombatLog> foundLogOpt = combatLogRepository.findById(newLog.getId());
        assertThat(foundLogOpt).isPresent();
        CombatLog foundLog = foundLogOpt.get();
        assertThat(foundLog.getPlayer1Id()).isEqualTo(player1.getId());
        assertThat(foundLog.getPlayer2Id()).isEqualTo(player2.getId());
        assertThat(foundLog.getWinnerId()).isEqualTo(player1.getId());
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve falhar ao salvar log com um jogador nulo")
    void shouldFailWhenSavingLogWithNullPlayer() {
        RpgCharacterEntity player1 = createAndPersistCharacter("Gimli", Race.DWARF);

        assertThrows(NullPointerException.class, () -> {
            CombatLog invalidLog = new CombatLog(player1, null, player1);
            combatLogRepository.saveAndFlush(invalidLog);
        });
    }

    @Test
    @Tag("PersistenceTest")
    @Tag("IntegrationTest")
    @DisplayName("Deve encontrar todos os logs de combate")
    void shouldFindAllCombatLogs() {
        RpgCharacterEntity p1 = createAndPersistCharacter("P1", Race.HUMAN);
        RpgCharacterEntity p2 = createAndPersistCharacter("P2", Race.ORC);
        RpgCharacterEntity p3 = createAndPersistCharacter("P3", Race.ELF);
        RpgCharacterEntity p4 = createAndPersistCharacter("P4", Race.DWARF);

        combatLogRepository.save(new CombatLog(p1, p2, p1));
        combatLogRepository.save(new CombatLog(p3, p4, p4));

        List<CombatLog> allLogs = combatLogRepository.findAll();

        assertThat(allLogs).hasSize(2);
    }

}