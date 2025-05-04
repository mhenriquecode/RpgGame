package br.ifsp.rpg;

import br.ifsp.web.controller.CharacterController;
import br.ifsp.web.controller.CombatController;
import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.service.CharacterService;
import br.ifsp.web.service.CombatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CombatControllerTest {
    @Mock CombatService service;
    @InjectMocks CombatController controller;

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
    @Tag(name = "Unit-test")
    @Tag(name = "TDD")
    @DisplayName("Should create combat test")
    void shouldCreateCombat() throws Exception {
        when(service.startCombat(any(), any(), any(), any())).thenReturn(createdCombat);

        mockMvc.perform(post("/api/combat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(combatDTO)))
                .andExpect(status().isCreated());
    }
}
