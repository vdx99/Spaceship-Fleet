package pl.vdx.spaceshipfleet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.vdx.spaceshipfleet.dto.SpaceshipRequestDto;
import pl.vdx.spaceshipfleet.dto.SpaceshipResponseDto;
import pl.vdx.spaceshipfleet.service.RequestCounterService;
import pl.vdx.spaceshipfleet.service.SpaceshipService;
import org.springframework.security.test.context.support.WithMockUser;


import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpaceshipController.class)
class SpaceshipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SpaceshipService spaceshipService;

    @MockBean
    private RequestCounterService requestCounterService;


    @WithMockUser
    @Test
    void create_shouldReturn200AndBody_whenRequestIsValid() throws Exception {
        // given
        SpaceshipRequestDto dto = new SpaceshipRequestDto();
        dto.setName("Falcon");
        dto.setClassType("FIGHTER");
        dto.setCrewCapacity(10);
        dto.setCurrentCrewCount(5);
        dto.setHomeBase("Earth");
        dto.setFuelLevelPercent(80.0);

        SpaceshipResponseDto response = new SpaceshipResponseDto(
                1L,
                "Falcon",
                "FIGHTER",
                10,
                5,
                "Earth",
                80.0
        );

        Mockito.when(spaceshipService.create(any(SpaceshipRequestDto.class)))
                .thenReturn(response);

        // when + then
        mockMvc.perform(post("/api/spaceships")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Falcon"))
                .andExpect(jsonPath("$.classType").value("FIGHTER"));
    }
    @WithMockUser
    @Test
    void create_shouldReturn400_whenRequestIsInvalid() throws Exception {
        // given: brak name, crewCapacity = 0, fuelLevelPercent > 100
        SpaceshipRequestDto dto = new SpaceshipRequestDto();
        dto.setName(" "); // @NotBlank naruszone
        dto.setClassType("FIGHTER");
        dto.setCrewCapacity(0); // @Min(1) naruszone
        dto.setCurrentCrewCount(5);
        dto.setFuelLevelPercent(150.0); // @DecimalMax("100.0") naruszone

        // when + then
        mockMvc.perform(post("/api/spaceships")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
