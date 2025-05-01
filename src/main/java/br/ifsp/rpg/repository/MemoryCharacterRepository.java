package br.ifsp.rpg.repository;

import br.ifsp.rpg.interfaces.CharacterRepository;
import br.ifsp.rpg.model.RpgCharacter;

import java.util.*;

public class MemoryCharacterRepository implements CharacterRepository {
    Map<UUID, RpgCharacter> characters = new HashMap<>();

    @Override
    public void save(RpgCharacter character) {
        characters.put(character.getId(), character);
    }

    @Override
    public Optional<RpgCharacter> findById(UUID id) {
        return Optional.ofNullable(characters.get(id));
    }

    @Override
    public List<RpgCharacter> findAll() {
        return new ArrayList<>(characters.values());
    }

    @Override
    public void update(RpgCharacter character) {
        characters.put(character.getId(), character);
    }


    @Override
    public void delete(UUID id) {
        characters.remove(id);
    }
}
