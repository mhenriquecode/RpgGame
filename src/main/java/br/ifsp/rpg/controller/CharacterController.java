package br.ifsp.rpg.controller;

import br.ifsp.rpg.dto.CharacterDTO;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.service.CharacterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {
    private final CharacterService characterService;

    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping
    public ResponseEntity<RpgCharacter> createCharacter(@Valid @RequestBody CharacterDTO characterDTO) {
        if(characterDTO.name() == null || characterDTO.name().isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return ResponseEntity.status(HttpStatus.CREATED).body(characterService.create(characterDTO));
    }
}
