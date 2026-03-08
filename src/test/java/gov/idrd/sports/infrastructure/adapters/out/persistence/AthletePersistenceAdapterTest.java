package gov.idrd.sports.infrastructure.adapters.out.persistence;

import gov.idrd.sports.domain.athlete.Athlete;
import gov.idrd.sports.infrastructure.adapters.out.persistence.jpa.entity.AthleteJpaEntity;
import gov.idrd.sports.infrastructure.adapters.out.persistence.jpa.repository.SpringDataAthleteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AthletePersistenceAdapterTest {

    @Mock
    private SpringDataAthleteRepository athleteRepository;

    private AthletePersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new AthletePersistenceAdapter(athleteRepository);
    }

    @Test
    void save_ShouldMapAndReturnSavedDomainObject() {
        Athlete athlete = Athlete.create("Maria", 22, "Sprint", 9L);

        AthleteJpaEntity savedEntity = new AthleteJpaEntity();
        savedEntity.setId(1L);
        savedEntity.setName("Maria");
        savedEntity.setAge(22);
        savedEntity.setCategory("Sprint");
        savedEntity.setTrainerId(9L);

        when(athleteRepository.save(any(AthleteJpaEntity.class))).thenReturn(savedEntity);

        Athlete savedAthlete = adapter.save(athlete);

        assertThat(savedAthlete.getId()).isEqualTo(1L);
        assertThat(savedAthlete.getName()).isEqualTo("Maria");
        assertThat(savedAthlete.getAge()).isEqualTo(22);
        assertThat(savedAthlete.getCategory()).isEqualTo("Sprint");
        assertThat(savedAthlete.getTrainerId()).isEqualTo(9L);
        verify(athleteRepository).save(any(AthleteJpaEntity.class));
    }

    @Test
    void findById_WhenExists_ShouldReturnMappedAthlete() {
        AthleteJpaEntity entity = new AthleteJpaEntity();
        entity.setId(2L);
        entity.setName("Ana");
        entity.setAge(19);
        entity.setCategory("Natacion");
        entity.setTrainerId(7L);

        when(athleteRepository.findById(2L)).thenReturn(Optional.of(entity));

        Optional<Athlete> result = adapter.findById(2L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(2L);
        assertThat(result.get().getName()).isEqualTo("Ana");
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        when(athleteRepository.findById(3L)).thenReturn(Optional.empty());

        Optional<Athlete> result = adapter.findById(3L);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_ShouldMapAllEntities() {
        AthleteJpaEntity first = new AthleteJpaEntity();
        first.setId(1L);
        first.setName("Maria");
        first.setAge(22);
        first.setCategory("Sprint");
        first.setTrainerId(9L);

        AthleteJpaEntity second = new AthleteJpaEntity();
        second.setId(2L);
        second.setName("Ana");
        second.setAge(19);
        second.setCategory("Natacion");
        second.setTrainerId(7L);

        when(athleteRepository.findAll()).thenReturn(List.of(first, second));

        List<Athlete> result = adapter.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Maria");
        assertThat(result.get(1).getName()).isEqualTo("Ana");
        verify(athleteRepository).findAll();
    }

    @Test
    void deleteById_ShouldDelegateToRepository() {
        adapter.deleteById(5L);

        verify(athleteRepository).deleteById(5L);
    }

    @Test
    void existsById_ShouldDelegateToRepository() {
        when(athleteRepository.existsById(6L)).thenReturn(true);

        boolean exists = adapter.existsById(6L);

        assertThat(exists).isTrue();
        verify(athleteRepository).existsById(6L);
    }
}
