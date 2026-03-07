package gov.idrd.sports.infrastructure.adapters.out.persistence;

import gov.idrd.sports.domain.athlete.Athlete;
import gov.idrd.sports.domain.athlete.port.AthleteRepositoryPort;
import gov.idrd.sports.infrastructure.adapters.out.persistence.jpa.entity.AthleteJpaEntity;
import gov.idrd.sports.infrastructure.adapters.out.persistence.jpa.repository.SpringDataAthleteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AthletePersistenceAdapter implements AthleteRepositoryPort {

    private final SpringDataAthleteRepository athleteRepository;

    public AthletePersistenceAdapter(SpringDataAthleteRepository athleteRepository) {
        this.athleteRepository = athleteRepository;
    }

    @Override
    public Athlete save(Athlete athlete) {
        AthleteJpaEntity toPersist = toJpaEntity(athlete);
        AthleteJpaEntity saved = athleteRepository.save(toPersist);
        return toDomain(saved);
    }

    @Override
    public Optional<Athlete> findById(Long id) {
        return athleteRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Athlete> findAll() {
        return athleteRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        athleteRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return athleteRepository.existsById(id);
    }

    private AthleteJpaEntity toJpaEntity(Athlete athlete) {
        AthleteJpaEntity entity = new AthleteJpaEntity();
        entity.setId(athlete.getId());
        entity.setName(athlete.getName());
        entity.setAge(athlete.getAge());
        entity.setCategory(athlete.getCategory());
        entity.setTrainerId(athlete.getTrainerId());
        return entity;
    }

    private Athlete toDomain(AthleteJpaEntity entity) {
        return Athlete.restore(
                entity.getId(),
                entity.getName(),
                entity.getAge(),
                entity.getCategory(),
                entity.getTrainerId());
    }
}
