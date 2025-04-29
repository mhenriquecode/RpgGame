package br.ifsp.rpg.service;

import br.ifsp.rpg.interfaces.CharacterRepository;
import br.ifsp.rpg.model.ClassType;
import br.ifsp.rpg.model.Race;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.model.Weapon;

import java.util.Optional;
import java.util.UUID;

public class CharacterService {
    private final CharacterRepository repository;

    public CharacterService(CharacterRepository repository) {
        this.repository = repository;
    }

    public void save(RpgCharacter character) {
        repository.save(character);
    }

    public Optional<RpgCharacter> getCharacter(UUID uuid) {
        return repository.findById(uuid);
    }


}
