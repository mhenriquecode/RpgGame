package br.ifsp.rpg.service;

import br.ifsp.rpg.interfaces.CharacterRepository;
import br.ifsp.rpg.model.ClassType;
import br.ifsp.rpg.model.Race;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.model.Weapon;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CharacterService {
    private final CharacterRepository repository;

    public CharacterService(CharacterRepository repository) {
        this.repository = repository;
    }

    public void save(RpgCharacter character) {
        if(character.getId() == null) throw new NullPointerException("Character id is null");
        if(character.getName() == null || character.getName().isEmpty())
            throw new NullPointerException("Character name is null or empty");
        if(character.getClassType() == null)
            throw new NullPointerException("Character class type is null");
        if(character.getRace() == null)
            throw new NullPointerException("Character race is null");
        if(character.getWeapon() == null)
            throw new NullPointerException("Character weapon is null");
        if(repository.findById(character.getId()).isPresent())
            throw new IllegalArgumentException("Character already exists");

        repository.save(character);
    }

    public Optional<RpgCharacter> getCharacter(UUID id) {
        if(repository.findById(id).isEmpty()) throw new NullPointerException("Character not found");
        return repository.findById(id);
    }


    public void update(UUID id, String newName, ClassType newClassType, Race newRace, Weapon newWeapon) {
        if(repository.findById(id).isEmpty()) throw new NullPointerException("Character not found");

        RpgCharacter character = repository.findById(id).get();
        if(newName != null && !newName.isEmpty()) character.setName(newName);
        if(newClassType != null) character.setClassType(newClassType);
        if(newRace != null) character.setRace(newRace);
        if(newWeapon != null) character.setWeapon(newWeapon);

        repository.update(character);
    }

    public void delete(UUID id) {
        if(repository.findById(id).isEmpty())
            throw new NullPointerException("Character not found");
        repository.delete(id);
    }
}
