package br.ifsp.rpg;

import br.ifsp.rpg.controller.CharacterController;
import br.ifsp.rpg.dto.CharacterDTO;
import br.ifsp.rpg.model.enums.ClassType;
import br.ifsp.rpg.model.enums.Race;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.model.enums.Weapon;
import br.ifsp.rpg.service.CharacterService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
}
