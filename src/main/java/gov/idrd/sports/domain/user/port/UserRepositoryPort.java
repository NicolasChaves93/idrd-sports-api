package gov.idrd.sports.domain.user.port;

import gov.idrd.sports.domain.user.User;

import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);
}
