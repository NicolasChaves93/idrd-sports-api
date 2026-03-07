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
        UserJpaEntity entity = toJpaEntity(user);
        UserJpaEntity saved = userRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(this::toDomain);
    }

    private UserJpaEntity toJpaEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setRole(user.getRole());
        entity.setPassword(user.getPassword());
        return entity;
    }

    private User toDomain(UserJpaEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setEmail(entity.getEmail());
        user.setRole(entity.getRole());
        user.setPassword(entity.getPassword());
        return user;
    }
}
