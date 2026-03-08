package gov.idrd.sports.application.athlete;

import gov.idrd.sports.application.athlete.dto.AthleteRequest;
import gov.idrd.sports.application.athlete.dto.AthleteResponse;
import gov.idrd.sports.domain.athlete.Athlete;
import gov.idrd.sports.domain.athlete.port.AthleteRepositoryPort;
import gov.idrd.sports.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AthleteApplicationServiceTest {

    @Mock
    private AthleteRepositoryPort athleteRepositoryPort;

    private AthleteApplicationService service;

    @BeforeEach
    void setUp() {
        service = new AthleteApplicationService(athleteRepositoryPort);
    }

    @Test
    void createAthlete_WithValidRequest_ShouldReturnResponse() {
        AthleteRequest request = new AthleteRequest("Maria", 22, "Sprint", 9L);
        Athlete savedAthlete = Athlete.restore(1L, "Maria", 22, "Sprint", 9L);
        when(athleteRepositoryPort.save(any(Athlete.class))).thenReturn(savedAthlete);

        AthleteResponse response = service.createAthlete(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Maria");
        assertThat(response.age()).isEqualTo(22);
        assertThat(response.category()).isEqualTo("Sprint");
        assertThat(response.trainerId()).isEqualTo(9L);
        verify(athleteRepositoryPort).save(any(Athlete.class));
    }

    @Test
    void getAthleteById_WhenExists_ShouldReturnResponse() {
        Athlete athlete = Athlete.restore(2L, "Ana", 19, "Natacion", 7L);
        when(athleteRepositoryPort.findById(2L)).thenReturn(Optional.of(athlete));

        AthleteResponse response = service.getAthleteById(2L);

        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.name()).isEqualTo("Ana");
        verify(athleteRepositoryPort).findById(2L);
    }

    @Test
    void getAthleteById_WhenDoesNotExist_ShouldThrowException() {
        when(athleteRepositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getAthleteById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Athlete with id 99 was not found");
    }

    @Test
    void getAllAthletes_ShouldMapAllAthletes() {
        Athlete first = Athlete.restore(1L, "Maria", 22, "Sprint", 9L);
        Athlete second = Athlete.restore(2L, "Ana", 19, "Natacion", 7L);
        when(athleteRepositoryPort.findAll()).thenReturn(List.of(first, second));

        List<AthleteResponse> responses = service.getAllAthletes();

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).name()).isEqualTo("Maria");
        assertThat(responses.get(1).name()).isEqualTo("Ana");
        verify(athleteRepositoryPort).findAll();
    }

    @Test
    void updateAthlete_WhenExists_ShouldUpdateAndReturnResponse() {
        Athlete existing = Athlete.restore(4L, "Maria", 22, "Sprint", 9L);
        AthleteRequest request = new AthleteRequest("Maria Paula", 23, "Salto", 10L);
        Athlete updated = Athlete.restore(4L, "Maria Paula", 23, "Salto", 10L);

        when(athleteRepositoryPort.findById(4L)).thenReturn(Optional.of(existing));
        when(athleteRepositoryPort.save(existing)).thenReturn(updated);

        AthleteResponse response = service.updateAthlete(4L, request);

        assertThat(response.id()).isEqualTo(4L);
        assertThat(response.name()).isEqualTo("Maria Paula");
        assertThat(response.age()).isEqualTo(23);
        assertThat(response.category()).isEqualTo("Salto");
        assertThat(response.trainerId()).isEqualTo(10L);
        verify(athleteRepositoryPort).findById(4L);
        verify(athleteRepositoryPort).save(existing);
    }

    @Test
    void updateAthlete_WhenDoesNotExist_ShouldThrowException() {
        AthleteRequest request = new AthleteRequest("Maria", 22, "Sprint", 9L);
        when(athleteRepositoryPort.findById(77L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateAthlete(77L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Athlete with id 77 was not found");

        verify(athleteRepositoryPort, never()).save(any(Athlete.class));
    }

    @Test
    void deleteAthlete_WhenExists_ShouldDeleteAthlete() {
        when(athleteRepositoryPort.existsById(3L)).thenReturn(true);

        service.deleteAthlete(3L);

        verify(athleteRepositoryPort).existsById(3L);
        verify(athleteRepositoryPort).deleteById(3L);
    }

    @Test
    void deleteAthlete_WhenDoesNotExist_ShouldThrowException() {
        when(athleteRepositoryPort.existsById(3L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteAthlete(3L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Athlete with id 3 was not found");

        verify(athleteRepositoryPort, never()).deleteById(3L);
    }
}
