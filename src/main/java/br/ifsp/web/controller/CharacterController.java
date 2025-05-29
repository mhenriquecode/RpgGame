package br.ifsp.web.controller;

import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.service.CharacterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/characters")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Characters", description = "RPG Character Manager")
public class CharacterController {
    private final CharacterService characterService;

    @Operation(summary = "Create new character")
    @PostMapping
    public ResponseEntity<CharacterDTO> createCharacter(@Valid @RequestBody CharacterDTO characterDTO) {
        CharacterDTO created = characterService.create(characterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Find character by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CharacterDTO> getCharacter(@PathVariable UUID id) {
        return characterService.getCharacter(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get all characters")
    @GetMapping
    public ResponseEntity<List<CharacterDTO>> getAllCharacters() {
        List<CharacterDTO> dtoList = characterService.getAllCharacters();
        return ResponseEntity.ok(dtoList);
    }

    @Operation(summary = "Update character")
    @PutMapping("/{id}")
    public ResponseEntity<CharacterDTO> updateCharacter(@PathVariable UUID id, @RequestBody @Valid CharacterDTO characterDTO) {
        CharacterDTO updated = characterService.update(id, characterDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete character by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable UUID id) {
        characterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
