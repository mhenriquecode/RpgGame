package br.ifsp.rpg.controller;

import br.ifsp.rpg.dto.CharacterDTO;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.service.CharacterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class CharacterController {
    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

//    @PostMapping
//    public ResponseEntity<RpgCharacter> createCharacter(@Valid @RequestBody CharacterDTO characterDTO) {
//        RpgCharacter created = characterService.create(characterDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(created);
//    }
}
