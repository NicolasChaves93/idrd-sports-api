package gov.idrd.sports.application.athlete.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AthleteRequestTest {

    @Test
    void constructor_ShouldAssignAllFields() {
        AthleteRequest request = new AthleteRequest("Maria", 22, "Sprint", 9L);

        assertThat(request.name()).isEqualTo("Maria");
        assertThat(request.age()).isEqualTo(22);
        assertThat(request.category()).isEqualTo("Sprint");
        assertThat(request.trainerId()).isEqualTo(9L);
    }

    @Test
    void equals_WithSameValues_ShouldBeTrue() {
        AthleteRequest first = new AthleteRequest("Maria", 22, "Sprint", 9L);
        AthleteRequest second = new AthleteRequest("Maria", 22, "Sprint", 9L);

        assertThat(first).isEqualTo(second);
        assertThat(first).hasSameHashCodeAs(second);
    }

    @Test
    void equals_WithDifferentValues_ShouldBeFalse() {
        AthleteRequest first = new AthleteRequest("Maria", 22, "Sprint", 9L);
        AthleteRequest second = new AthleteRequest("Ana", 18, "Natacion", 7L);

        assertThat(first).isNotEqualTo(second);
    }

    @Test
    void toString_ShouldContainFieldValues() {
        AthleteRequest request = new AthleteRequest("Maria", 22, "Sprint", 9L);

        assertThat(request.toString()).contains("Maria", "22", "Sprint", "9");
    }
}
