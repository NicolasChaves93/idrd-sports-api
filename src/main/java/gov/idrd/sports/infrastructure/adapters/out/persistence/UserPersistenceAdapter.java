package gov.idrd.sports.infrastructure.adapters.out.persistence;

import gov.idrd.sports.domain.user.User;
import gov.idrd.sports.domain.user.port.UserRepositoryPort;
import gov.idrd.sports.infrastructure.adapters.out.persistence.jpa.entity.UserJpaEntity;
import gov.idrd.sports.infrastructure.adapters.out.persistence.jpa.repository.SpringDataUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserPersistenceAdapter implements UserRepositoryPort {

    private final SpringDataUserRepository userRepository;

    public UserPersistenceAdapter(SpringDataUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setRole(user.getRole());
        entity.setPassword(user.getPassword()); // BAD: saving plain password

        UserJpaEntity saved = userRepository.save(entity);

        // BAD: Code duplication - same mapping logic
        User result = new User();
        result.setId(saved.getId());
        result.setName(saved.getName());
        result.setEmail(saved.getEmail());
        result.setRole(saved.getRole());
        result.setPassword(saved.getPassword());

        return result;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserJpaEntity> entity = userRepository.findByEmail(email);
        if (entity.isPresent()) {
            // BAD: Code duplication
            User user = new User();
            user.setId(entity.get().getId());
            user.setName(entity.get().getName());
            user.setEmail(entity.get().getEmail());
            user.setRole(entity.get().getRole());
            user.setPassword(entity.get().getPassword());
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<UserJpaEntity> entity = userRepository.findById(id);
        if (entity.isPresent()) {
            // BAD: Code duplication (third time!)
            User user = new User();
            user.setId(entity.get().getId());
            user.setName(entity.get().getName());
            user.setEmail(entity.get().getEmail());
            user.setRole(entity.get().getRole());
            user.setPassword(entity.get().getPassword());
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
