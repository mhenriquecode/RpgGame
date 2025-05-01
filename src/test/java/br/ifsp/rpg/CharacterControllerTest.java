package br.ifsp.rpg;
import br.ifsp.rpg.model.ClassType;
import br.ifsp.rpg.model.Race;
import br.ifsp.rpg.model.RpgCharacter;
import br.ifsp.rpg.model.Weapon;
import br.ifsp.rpg.service.CharacterService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pitest.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CharacterControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private CharacterService characterService;

    @Test
    @DisplayName("Should create a character successfully test")
    void shouldCreateCharacterTest() throws Exception {
        CharacterRequestDTO request = new CharacterRequestDTO("Character", ClassType.WARRIOR, "Orc", "Axe");

        RpgCharacter createdCharacter = new RpgCharacter(
                "Character",
                ClassType.WARRIOR,
                new Race("Orc", 0, 5,0, 0),
                new Weapon("Axe", 2, 6));

        when(characterService.create(any())).thenReturn(createdCharacter);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Character"));
    }
}
