package gov.idrd.sports.infrastructure.adapters.out.persistence.jpa.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AthleteJpaEntityTest {

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        AthleteJpaEntity entity = new AthleteJpaEntity();

        entity.setId(3L);
        entity.setName("Maria");
        entity.setAge(22);
        entity.setCategory("Sprint");
        entity.setTrainerId(11L);

        assertThat(entity.getId()).isEqualTo(3L);
        assertThat(entity.getName()).isEqualTo("Maria");
        assertThat(entity.getAge()).isEqualTo(22);
        assertThat(entity.getCategory()).isEqualTo("Sprint");
        assertThat(entity.getTrainerId()).isEqualTo(11L);
    }

    @Test
    void defaultConstructor_ShouldCreateEmptyEntity() {
        AthleteJpaEntity entity = new AthleteJpaEntity();

        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isNull();
        assertThat(entity.getAge()).isNull();
        assertThat(entity.getCategory()).isNull();
        assertThat(entity.getTrainerId()).isNull();
    }
}
