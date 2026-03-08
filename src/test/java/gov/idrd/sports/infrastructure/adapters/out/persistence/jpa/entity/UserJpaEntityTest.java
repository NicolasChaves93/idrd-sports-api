package gov.idrd.sports.infrastructure.adapters.out.persistence.jpa.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserJpaEntityTest {

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        UserJpaEntity entity = new UserJpaEntity();

        // When
        entity.setId(1L);
        entity.setName("John Doe");
        entity.setEmail("john@idrd.gov.co");
        entity.setRole("ADMIN");
        entity.setPassword("hashedPassword");

        // Then
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("John Doe");
        assertThat(entity.getEmail()).isEqualTo("john@idrd.gov.co");
        assertThat(entity.getRole()).isEqualTo("ADMIN");
        assertThat(entity.getPassword()).isEqualTo("hashedPassword");
    }

    @Test
    void defaultConstructor_ShouldCreateEmptyEntity() {
        // When
        UserJpaEntity entity = new UserJpaEntity();

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isNull();
        assertThat(entity.getEmail()).isNull();
        assertThat(entity.getRole()).isNull();
        assertThat(entity.getPassword()).isNull();
    }
}
