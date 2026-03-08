package gov.idrd.sports.infrastructure.adapters.out.persistence;

import gov.idrd.sports.domain.user.User;
import gov.idrd.sports.infrastructure.adapters.out.persistence.jpa.entity.UserJpaEntity;
import gov.idrd.sports.infrastructure.adapters.out.persistence.jpa.repository.SpringDataUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPersistenceAdapterTest {

    @Mock
    private SpringDataUserRepository userRepository;

    private UserPersistenceAdapter userPersistenceAdapter;

    @BeforeEach
    void setUp() {
        userPersistenceAdapter = new UserPersistenceAdapter(userRepository);
    }

    @Test
    void save_ShouldPersistUserAndReturnDomainObject() {
        // Given
        User user = new User(null, "John Doe", "john@idrd.gov.co", "ADMIN", "hashedPassword");
        UserJpaEntity savedEntity = new UserJpaEntity();
        savedEntity.setId(1L);
        savedEntity.setName("John Doe");
        savedEntity.setEmail("john@idrd.gov.co");
        savedEntity.setRole("ADMIN");
        savedEntity.setPassword("hashedPassword");

        when(userRepository.save(any(UserJpaEntity.class))).thenReturn(savedEntity);

        // When
        User result = userPersistenceAdapter.save(user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john@idrd.gov.co");
        assertThat(result.getRole()).isEqualTo("ADMIN");
        verify(userRepository).save(any(UserJpaEntity.class));
    }

    @Test
    void findByEmail_WhenExists_ShouldReturnUser() {
        // Given
        String email = "admin@idrd.gov.co";
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(1L);
        entity.setName("Admin User");
        entity.setEmail(email);
        entity.setRole("ADMIN");
        entity.setPassword("hashedPassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(entity));

        // When
        Optional<User> result = userPersistenceAdapter.findByEmail(email);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(email);
        assertThat(result.get().getName()).isEqualTo("Admin User");
        verify(userRepository).findByEmail(email);
    }

    @Test
    void findByEmail_WhenNotExists_ShouldReturnEmpty() {
        // Given
        String email = "nonexistent@idrd.gov.co";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userPersistenceAdapter.findByEmail(email);

        // Then
        assertThat(result).isEmpty();
        verify(userRepository).findByEmail(email);
    }

    @Test
    void findById_WhenExists_ShouldReturnUser() {
        // Given
        Long userId = 1L;
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(userId);
        entity.setName("User Name");
        entity.setEmail("user@idrd.gov.co");
        entity.setRole("USER");
        entity.setPassword("hashedPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(entity));

        // When
        Optional<User> result = userPersistenceAdapter.findById(userId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(userId);
        assertThat(result.get().getName()).isEqualTo("User Name");
        verify(userRepository).findById(userId);
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        // Given
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userPersistenceAdapter.findById(userId);

        // Then
        assertThat(result).isEmpty();
        verify(userRepository).findById(userId);
    }
}
