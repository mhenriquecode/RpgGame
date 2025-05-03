package br.ifsp.rpg;

import br.ifsp.web.controller.CharacterController;
import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.exception.CharacterNotFoundException;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.service.CharacterService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class CharacterControllerTest {
    @Mock CharacterService service;
    @InjectMocks CharacterController controller;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    private RpgCharacter createdCharacter;
    private CharacterDTO characterDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .defaultRequest(get("/").accept(MediaType.APPLICATION_JSON))
                .build();

        createdCharacter = new RpgCharacter("Character", ClassType.WARRIOR, Race.ORC, Weapon.AXE);
        characterDTO = new CharacterDTO("Character", ClassType.WARRIOR, Race.ORC, Weapon.AXE);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Should create a character successfully test")
    void shouldCreateCharacterTest() throws Exception {
        when(service.create(any())).thenReturn(createdCharacter);

        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(characterDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Character"));
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Should not create invalid character test")
    void shouldNotCreateInvalidCharacterTest() throws Exception {
        CharacterDTO invalidCharacter = new CharacterDTO("InvalidChar", null, Race.DWARF, Weapon.AXE);

        doThrow(new IllegalArgumentException("ClassType cannot be null"))
                .when(service).create(any());

        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidCharacter)))
                .andExpect(status().isBadRequest());

        verify(service).create(any());
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Should return character by ID test")
    void shouldReturnCharacterById() throws Exception {
        when(service.getCharacter(createdCharacter.getId())).thenReturn(Optional.of(createdCharacter));

        mockMvc.perform(get("/api/characters/{id}", createdCharacter.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Character"))
                .andExpect(jsonPath("$.classType").value("WARRIOR"))
                .andExpect(jsonPath("$.race").value("ORC"))
                .andExpect(jsonPath("$.weapon").value("AXE"));
    }

    @Test
    @Tag("Unit-Test")
    @DisplayName("Should return 404 if character not found test")
    void shouldReturn404WhenCharacterNotFoundTest() throws Exception {
        UUID nonExistentId = UUID.randomUUID();
        doThrow(new CharacterNotFoundException(nonExistentId)).when(service).delete(nonExistentId);

        mockMvc.perform(get("/api/characters/{id}", nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Should update character successfully test")
    void shouldUpdateCharacterTest() throws Exception {
        UUID characterId = UUID.randomUUID();

        CharacterDTO updatedDTO = new CharacterDTO(
                "UpdatedName",
                ClassType.PALADIN,
                Race.ELF,
                Weapon.DAGGER);

        RpgCharacter updatedCharacter = new RpgCharacter(
                "UpdatedName",
                ClassType.PALADIN,
                Race.ELF,
                Weapon.DAGGER);

        when(service.getCharacter(characterId)).thenReturn(Optional.of(updatedCharacter));
        when(service.update(eq(characterId), any(CharacterDTO.class))).thenReturn(updatedCharacter);

        mockMvc.perform(put("/api/characters/{id}", characterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UpdatedName"))
                .andExpect(jsonPath("$.classType").value("PALADIN"))
                .andExpect(jsonPath("$.race").value("ELF"))
                .andExpect(jsonPath("$.weapon").value("DAGGER"));
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Should not update character with invalid data test")
    void shouldNotUpdateCharacterWithInvalidDataTest() throws Exception {
        UUID characterId = UUID.randomUUID();
        CharacterDTO invalidCharacter = new CharacterDTO("InvalidChar", null, Race.DWARF, Weapon.AXE);

        doThrow(new IllegalArgumentException("ClassType cannot be null"))
                .when(service).update(eq(characterId), any());

        mockMvc.perform(put("/api/characters/{id}", characterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidCharacter)))
                .andExpect(status().isBadRequest());

        verify(service).update(eq(characterId), any());
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Should delete character successfully test")
    void shouldDeleteCharacterTest() throws Exception {
        UUID characterId = UUID.randomUUID();

        doNothing().when(service).delete(characterId);

        mockMvc.perform(delete("/api/characters/{id}", characterId))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(characterId);
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Should return all characters test")
    void shouldReturnAllCharacters() throws Exception {
        List<RpgCharacter> characters = List.of(
                new RpgCharacter("Character1", ClassType.WARRIOR, Race.HUMAN, Weapon.SWORD),
                new RpgCharacter("Character2", ClassType.PALADIN, Race.ELF, Weapon.DAGGER)
        );

        when(service.getAllCharacters()).thenReturn(characters);

        mockMvc.perform(get("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Character1"))
                .andExpect(jsonPath("$[1].name").value("Character2"));
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Should return empty list when no characters are registered")
    void shouldReturnEmptyListWhenNoCharactersExist() throws Exception {
        when(service.getAllCharacters()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/characters"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }
}
