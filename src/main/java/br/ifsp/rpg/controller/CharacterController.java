package br.ifsp.rpg.controller;

import br.ifsp.rpg.dto.CharacterDTO;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.service.CharacterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {
    private final CharacterService characterService;

    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping
    public ResponseEntity<CharacterDTO> createCharacter(@Valid @RequestBody CharacterDTO characterDTO) {
        if(characterDTO.name() == null || characterDTO.name().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.CREATED).body(CharacterDTO.from(characterService.create(characterDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterDTO> getCharacter(@PathVariable UUID id) {
        return characterService.getCharacter(id)
                .map(character -> ResponseEntity.ok(CharacterDTO.from(character)))
                .orElse(ResponseEntity.notFound().build());
    }
}
