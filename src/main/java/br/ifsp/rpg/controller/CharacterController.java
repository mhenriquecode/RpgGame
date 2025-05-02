package br.ifsp.rpg.controller;

import br.ifsp.rpg.dto.CharacterDTO;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.service.CharacterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/characters")
@AllArgsConstructor
@Tag(name = "Characters", description = "RPG Character Manager")
public class CharacterController {
    private final CharacterService characterService;

    @Operation(summary = "Create new character")
    @PostMapping
    public ResponseEntity<CharacterDTO> createCharacter(@Valid @RequestBody CharacterDTO characterDTO) {
        if(characterDTO.name() == null || characterDTO.name().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.CREATED).body(CharacterDTO.from(characterService.create(characterDTO)));
    }

    @Operation(summary = "Find character by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CharacterDTO> getCharacter(@PathVariable UUID id) {
        return characterService.getCharacter(id)
                .map(character -> ResponseEntity.ok(CharacterDTO.from(character)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all characters")
    @GetMapping
    public ResponseEntity<List<CharacterDTO>> getAllCharacters() {
        List<CharacterDTO> dtoList = characterService.getAllCharacters().stream()
                .map(CharacterDTO::from).toList();

        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "Update character")
    @PutMapping("/{id}")
    public ResponseEntity<CharacterDTO> updateCharacter(@PathVariable UUID id, @RequestBody @Valid CharacterDTO characterDTO) {
        if(characterService.getCharacter(id).isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return ResponseEntity.ok(CharacterDTO.from(characterService.update(id, characterDTO)));
    }

    @Operation(summary = "Delete character by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable UUID id) {
        characterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
