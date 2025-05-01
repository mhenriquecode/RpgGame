package br.ifsp.rpg.interfaces;

import br.ifsp.rpg.model.RpgCharacter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CharacterRepository {
    void save(RpgCharacter character);
    Optional<RpgCharacter> findById(UUID id);
    List<RpgCharacter> findAll();
    void update(RpgCharacter character);
    void delete(UUID id);
}
