package gov.idrd.sports.application.athlete.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AthleteResponseTest {

    @Test
    void constructor_ShouldAssignAllFields() {
        AthleteResponse response = new AthleteResponse(5L, "Maria", 22, "Sprint", 9L);

        assertThat(response.id()).isEqualTo(5L);
        assertThat(response.name()).isEqualTo("Maria");
        assertThat(response.age()).isEqualTo(22);
        assertThat(response.category()).isEqualTo("Sprint");
        assertThat(response.trainerId()).isEqualTo(9L);
    }

    @Test
    void equals_WithSameValues_ShouldBeTrue() {
        AthleteResponse first = new AthleteResponse(5L, "Maria", 22, "Sprint", 9L);
        AthleteResponse second = new AthleteResponse(5L, "Maria", 22, "Sprint", 9L);

        assertThat(first).isEqualTo(second);
        assertThat(first).hasSameHashCodeAs(second);
    }

    @Test
    void equals_WithDifferentValues_ShouldBeFalse() {
        AthleteResponse first = new AthleteResponse(5L, "Maria", 22, "Sprint", 9L);
        AthleteResponse second = new AthleteResponse(6L, "Ana", 18, "Natacion", 7L);

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void toString_ShouldContainFieldValues() {
        AthleteResponse response = new AthleteResponse(5L, "Maria", 22, "Sprint", 9L);

        assertThat(response.toString()).contains("5", "Maria", "22", "Sprint", "9");
    }
}
