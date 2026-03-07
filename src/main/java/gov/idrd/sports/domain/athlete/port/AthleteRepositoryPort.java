package gov.idrd.sports.domain.athlete.port;

import gov.idrd.sports.domain.athlete.Athlete;

import java.util.List;
import java.util.Optional;

public interface AthleteRepositoryPort {

    Athlete save(Athlete athlete);

    Optional<Athlete> findById(Long id);

    List<Athlete> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);
}
