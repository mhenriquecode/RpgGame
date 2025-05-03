package br.ifsp.web.service;

import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.exceptions.CharacterNotFoundException;
import br.ifsp.web.repository.CharacterRepository;
import br.ifsp.web.model.RpgCharacter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CharacterService {
    private final CharacterRepository repository;

    public RpgCharacter create(CharacterDTO characterDTO) {
        if(characterDTO.name() == null || characterDTO.name().isEmpty())
            throw new IllegalArgumentException("Name cannot be empty or null");
        if(characterDTO.classType() == null)
            throw new IllegalArgumentException("ClassType cannot be null");
        if(characterDTO.race() == null)
            throw new IllegalArgumentException("Race cannot be null");
        if(characterDTO.weapon() == null)
            throw new IllegalArgumentException("Weapon cannot be null");

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
        RpgCharacter character = repository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));

        repository.delete(character);
    }
}
