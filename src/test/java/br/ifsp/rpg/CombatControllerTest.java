package br.ifsp.rpg;

import br.ifsp.web.controller.CharacterController;
import br.ifsp.web.controller.CombatController;
import br.ifsp.web.dto.CharacterDTO;
import br.ifsp.web.interfaces.ChooseAction;
import br.ifsp.web.model.Combat;
import br.ifsp.web.model.RpgCharacter;
import br.ifsp.web.model.actions.ChooseUserAction;
import br.ifsp.web.model.enums.ClassType;
import br.ifsp.web.model.enums.Race;
import br.ifsp.web.model.enums.Weapon;
import br.ifsp.web.service.CharacterService;
import br.ifsp.web.service.CombatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CombatControllerTest {
    @Mock CombatService service;
    @InjectMocks CombatController controller;

    private Combat combat;
    private ChooseAction attackAction;
    private ChooseAction defendAction;
    private RpgCharacter player1;
    private RpgCharacter player2;

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

        RpgCharacter player1 = new RpgCharacter("P1", ClassType.PALADIN, Race.DWARF, Weapon.AXE);
        RpgCharacter player2 = new RpgCharacter("P2", ClassType.BERSERK, Race.ELF, Weapon.SWORD);

        attackAction = new ChooseUserAction(1);
        defendAction = new ChooseUserAction(2);
        combat = new Combat(player1, attackAction, player2, attackAction);

    }

    @Test
    @Tag("Unit-test")
    @Tag("TDD")
    @DisplayName("Should start a combat successfully")
    void shouldStartCombatSuccessfully() throws Exception {
        when(service.startCombat(player1, attackAction, player2, attackAction))
                .thenReturn(combat);

        mockMvc.perform(post("/api/combat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(combat)))
                .andExpect(status().isOk());

        verify(service, times(1))
                .startCombat(player1, attackAction, player2, attackAction);
    }
}
