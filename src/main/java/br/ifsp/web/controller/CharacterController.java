package br.ifsp.web.controller;

import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.model.RpgCharacter;
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
@RequestMapping(path = "/api/characters")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Characters", description = "RPG Character Manager")
public class CharacterController {
    private final CharacterService characterService;

    @Operation(summary = "Create new character")
    @PostMapping
    public ResponseEntity<CharacterDTO> createCharacter(@Valid @RequestBody CharacterDTO characterDTO) {
        try {
            RpgCharacter created = characterService.create(characterDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(CharacterDTO.from(created));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
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
