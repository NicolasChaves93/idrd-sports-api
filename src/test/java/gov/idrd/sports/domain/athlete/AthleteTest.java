package gov.idrd.sports.domain.athlete;

import gov.idrd.sports.domain.exception.DomainValidationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AthleteTest {

    @Test
    void create_WithValidValues_ShouldCreateAthlete() {
        Athlete athlete = Athlete.create(" Maria Perez ", 21, " Sprint ", 10L);

        assertThat(athlete.getId()).isNull();
        assertThat(athlete.getName()).isEqualTo("Maria Perez");
        assertThat(athlete.getAge()).isEqualTo(21);
        assertThat(athlete.getCategory()).isEqualTo("Sprint");
        assertThat(athlete.getTrainerId()).isEqualTo(10L);
    }

    @Test
    void restore_WithValidValues_ShouldCreateAthleteWithId() {
        Athlete athlete = Athlete.restore(7L, "Ana", 18, "Natacion", 4L);

        assertThat(athlete.getId()).isEqualTo(7L);
        assertThat(athlete.getName()).isEqualTo("Ana");
        assertThat(athlete.getAge()).isEqualTo(18);
        assertThat(athlete.getCategory()).isEqualTo("Natacion");
        assertThat(athlete.getTrainerId()).isEqualTo(4L);
    }

    @Test
    void restore_WithNullId_ShouldThrowException() {
        assertThatThrownBy(() -> Athlete.restore(null, "Ana", 18, "Natacion", 4L))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Athlete id must be a positive value");
    }

    @Test
    void restore_WithNonPositiveId_ShouldThrowException() {
        assertThatThrownBy(() -> Athlete.restore(0L, "Ana", 18, "Natacion", 4L))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Athlete id must be a positive value");
    }

    @Test
    void create_WithBlankName_ShouldThrowException() {
        assertThatThrownBy(() -> Athlete.create("   ", 18, "Natacion", 4L))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Athlete name is required");
    }

    @Test
    void create_WithNullAge_ShouldThrowException() {
        assertThatThrownBy(() -> Athlete.create("Ana", null, "Natacion", 4L))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Athlete age must be greater than zero");
    }

    @Test
    void create_WithZeroAge_ShouldThrowException() {
        assertThatThrownBy(() -> Athlete.create("Ana", 0, "Natacion", 4L))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Athlete age must be greater than zero");
    }

    @Test
    void create_WithBlankCategory_ShouldThrowException() {
        assertThatThrownBy(() -> Athlete.create("Ana", 18, "", 4L))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Athlete category is required");
    }

    @Test
    void updateProfile_WithValidValues_ShouldUpdateAthlete() {
        Athlete athlete = Athlete.restore(7L, "Ana", 18, "Natacion", 4L);

        athlete.updateProfile(" Ana Maria ", 19, " Atletas ", 8L);

        assertThat(athlete.getName()).isEqualTo("Ana Maria");
        assertThat(athlete.getAge()).isEqualTo(19);
        assertThat(athlete.getCategory()).isEqualTo("Atletas");
        assertThat(athlete.getTrainerId()).isEqualTo(8L);
    }

    @Test
    void updateProfile_WithInvalidValues_ShouldThrowException() {
        Athlete athlete = Athlete.restore(7L, "Ana", 18, "Natacion", 4L);

        assertThatThrownBy(() -> athlete.updateProfile("", 19, "Atletas", 8L))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("Athlete name is required");
    }
}
