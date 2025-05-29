package br.ifsp.web.service;

import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.exception.CharacterNotFoundException;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.repository.CharacterRepository;
import br.ifsp.web.repository.RpgCharacterEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CharacterService {
    private final CharacterRepository repository;

    public CharacterDTO create(CharacterDTO characterDTO) {
        RpgCharacter domain = characterDTO.toEntity();
        RpgCharacterEntity saved = repository.save(toEntity(domain));
        return CharacterDTO.from(toDomain(saved));
    }


    public Optional<CharacterDTO> getCharacter(UUID id) {
        return repository.findById(id)
                .map(this::toDomain)
                .map(CharacterDTO::from);
    }

    public List<CharacterDTO> getAllCharacters() {
        return repository.findAll().stream()
                .map(this::toDomain)
                .map(CharacterDTO::from)
                .toList();
    }

    public CharacterDTO update(UUID id, CharacterDTO characterDTO) {
        RpgCharacterEntity entity = repository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));

        entity.setName(characterDTO.name());
        entity.setClassType(characterDTO.classType());
        entity.setRace(characterDTO.race());
        entity.setWeapon(characterDTO.weapon());

        RpgCharacterEntity saved = repository.save(entity);
        return CharacterDTO.from(toDomain(saved));
    }

    public void delete(UUID id) {
        RpgCharacterEntity entity = repository.findById(id)
                .orElseThrow(() -> new CharacterNotFoundException(id));
        repository.delete(entity);
    }

    private RpgCharacterEntity toEntity(RpgCharacter domain) {
        RpgCharacterEntity entity = new RpgCharacterEntity(
                domain.getName(),
                domain.getClassType(),
                domain.getRace(),
                domain.getWeapon()
        );
        entity.setId(domain.getId());
        return entity;
    }

    private RpgCharacter toDomain(RpgCharacterEntity entity) {
        RpgCharacter character = new RpgCharacter(
                entity.getName(),
                entity.getClassType(),
                entity.getRace(),
                entity.getWeapon()
        );
        character.setMaxHealth(character.getMaxHealth());
        character.setHealth(character.getMaxHealth());

        try {
            Field idField = RpgCharacter.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(character, entity.getId());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao setar ID no RpgCharacter", e);
        }

        return character;
    }

    public Optional<RpgCharacter> getCharacterDomain(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }
}
