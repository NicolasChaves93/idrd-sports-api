package gov.idrd.sports.infrastructure.adapters.out.persistence.jpa.repository;

import gov.idrd.sports.infrastructure.adapters.out.persistence.jpa.entity.AthleteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAthleteRepository extends JpaRepository<AthleteJpaEntity, Long> {
}
