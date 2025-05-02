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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CharacterControllerTest {
    @Mock CharacterService service;
    @InjectMocks CharacterController controller;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .defaultRequest(get("/").accept(MediaType.APPLICATION_JSON))
                .build();
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Should create a character successfully test")
    void shouldCreateCharacterTest() throws Exception {
        CharacterDTO characterDTO = new CharacterDTO("Character", ClassType.WARRIOR, Race.ORC, Weapon.AXE);

        RpgCharacter createdCharacter = new RpgCharacter(
                "Character",
                ClassType.WARRIOR,
                Race.ORC,
                Weapon.AXE
        );

        when(service.create(any())).thenReturn(createdCharacter);

        String json = mapper.writeValueAsString(characterDTO);

        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Character"));
    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Should return character by ID test")
    void shouldReturnCharacterById() throws Exception {
        RpgCharacter character = new RpgCharacter(
                "Character",
                ClassType.WARRIOR,
                Race.HUMAN,
                Weapon.SWORD
        );

        when(service.getCharacter(character.getId())).thenReturn(Optional.of(character));

        mockMvc.perform(get("/characters/{id}", character.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("CharacterName"))
                .andExpect(jsonPath("$.classType").value("WARRIOR"))
                .andExpect(jsonPath("$.race").value("HUMAN"))
                .andExpect(jsonPath("$.weapon").value("SWORD"));
    }
}
