package gov.idrd.sports.infrastructure.adapters.in.web;

import gov.idrd.sports.application.athlete.AthleteUseCase;
import gov.idrd.sports.application.athlete.dto.AthleteRequest;
import gov.idrd.sports.application.athlete.dto.AthleteResponse;
import gov.idrd.sports.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AthleteController.class)
class AthleteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AthleteUseCase athleteUseCase;

    @Test
    @WithMockUser
    void createAthlete_WithValidData_ShouldReturnCreated() throws Exception {
        AthleteResponse response = new AthleteResponse(1L, "Maria", 22, "Sprint", 9L);
        when(athleteUseCase.createAthlete(any(AthleteRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/athletes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Maria\",\"age\":22,\"category\":\"Sprint\",\"trainerId\":9}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Maria"));

        verify(athleteUseCase).createAthlete(any(AthleteRequest.class));
    }

    @ParameterizedTest
    @CsvSource({
            "'{\"name\":\"\",\"age\":22,\"category\":\"Sprint\",\"trainerId\":9}'",
            "'{\"name\":\"Maria\",\"age\":0,\"category\":\"Sprint\",\"trainerId\":9}'",
            "'{\"name\":\"Maria\",\"age\":22,\"category\":\"\",\"trainerId\":9}'"
    })
    @WithMockUser
    void createAthlete_WithInvalidData_ShouldReturnBadRequest(String jsonContent) throws Exception {
        mockMvc.perform(post("/api/athletes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isBadRequest());

        verify(athleteUseCase, never()).createAthlete(any(AthleteRequest.class));
    }

    @Test
    @WithMockUser
    void getAthletes_ShouldReturnOkAndList() throws Exception {
        when(athleteUseCase.getAllAthletes()).thenReturn(List.of(
                new AthleteResponse(1L, "Maria", 22, "Sprint", 9L),
                new AthleteResponse(2L, "Ana", 19, "Natacion", 7L)));

        mockMvc.perform(get("/api/athletes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Maria"))
                .andExpect(jsonPath("$[1].name").value("Ana"));

        verify(athleteUseCase).getAllAthletes();
    }

    @Test
    @WithMockUser
    void getAthleteById_WhenFound_ShouldReturnOk() throws Exception {
        when(athleteUseCase.getAthleteById(2L)).thenReturn(new AthleteResponse(2L, "Ana", 19, "Natacion", 7L));

        mockMvc.perform(get("/api/athletes/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Ana"));

        verify(athleteUseCase).getAthleteById(2L);
    }

    @Test
    @WithMockUser
    void getAthleteById_WhenNotFound_ShouldReturnNotFound() throws Exception {
        when(athleteUseCase.getAthleteById(77L))
                .thenThrow(new ResourceNotFoundException("Athlete with id 77 was not found"));

        mockMvc.perform(get("/api/athletes/77"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Athlete with id 77 was not found"));
    }

    @Test
    @WithMockUser
    void updateAthlete_WithValidData_ShouldReturnOk() throws Exception {
        when(athleteUseCase.updateAthlete(any(Long.class), any(AthleteRequest.class)))
                .thenReturn(new AthleteResponse(1L, "Maria Paula", 23, "Salto", 10L));

        mockMvc.perform(put("/api/athletes/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Maria Paula\",\"age\":23,\"category\":\"Salto\",\"trainerId\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maria Paula"));

        verify(athleteUseCase).updateAthlete(any(Long.class), any(AthleteRequest.class));
    }

    @Test
    @WithMockUser
    void updateAthlete_WhenNotFound_ShouldReturnNotFound() throws Exception {
        when(athleteUseCase.updateAthlete(any(Long.class), any(AthleteRequest.class)))
                .thenThrow(new ResourceNotFoundException("Athlete with id 99 was not found"));

        mockMvc.perform(put("/api/athletes/99")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Maria\",\"age\":22,\"category\":\"Sprint\",\"trainerId\":9}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Athlete with id 99 was not found"));
    }

    @Test
    @WithMockUser
    void deleteAthlete_WhenFound_ShouldReturnNoContent() throws Exception {
        doNothing().when(athleteUseCase).deleteAthlete(1L);

        mockMvc.perform(delete("/api/athletes/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(athleteUseCase).deleteAthlete(1L);
    }

    @Test
    @WithMockUser
    void deleteAthlete_WhenNotFound_ShouldReturnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Athlete with id 44 was not found"))
                .when(athleteUseCase).deleteAthlete(44L);

        mockMvc.perform(delete("/api/athletes/44").with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Athlete with id 44 was not found"));
    }
}
