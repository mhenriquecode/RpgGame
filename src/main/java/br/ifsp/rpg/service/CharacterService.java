package br.ifsp.rpg.service;

import br.ifsp.rpg.dto.CharacterDTO;
import br.ifsp.rpg.interfaces.CharacterRepository;
import br.ifsp.rpg.model.enums.ClassType;
import br.ifsp.rpg.model.enums.Race;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.model.enums.Weapon;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CharacterService {
    private final CharacterRepository repository;

    public CharacterService(CharacterRepository repository) {
        this.repository = repository;
    }

    public RpgCharacter create(CharacterDTO characterDTO) {
        RpgCharacter character = new RpgCharacter(characterDTO.name(), characterDTO.classType(), characterDTO.race(), characterDTO.weapon());
        repository.save(character);

        return character;
    }

    public void save(RpgCharacter character) {
        if(character.getId() == null)
            throw new NullPointerException("Character id is null");
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
        if(repository.findById(id).isEmpty())
            throw new IllegalArgumentException("Character not found");

        return repository.findById(id);
    }

    public List<RpgCharacter> getAllCharacters() {
        return repository.findAll();
    }

    public RpgCharacter update(UUID id, CharacterDTO characterDTO) {
        if(repository.findById(id).isEmpty()) throw new IllegalArgumentException("Character not found");
        RpgCharacter character = repository.findById(id).get();
        character.setName(characterDTO.name());
        character.setClassType(characterDTO.classType());
        character.setRace(characterDTO.race());
        character.setWeapon(characterDTO.weapon());
        repository.save(character);

        return character;
    }

    public void delete(UUID id) {
        if(repository.findById(id).isEmpty())
            throw new NullPointerException("Character not found");

        repository.delete(id);
    }
}
