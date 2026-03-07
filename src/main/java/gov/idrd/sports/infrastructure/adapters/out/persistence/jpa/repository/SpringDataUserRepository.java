package gov.idrd.sports.infrastructure.adapters.out.persistence.jpa.repository;

import gov.idrd.sports.infrastructure.adapters.out.persistence.jpa.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataUserRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByEmail(String email);
}
