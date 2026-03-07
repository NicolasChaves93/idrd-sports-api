package gov.idrd.sports.application.athlete;

import gov.idrd.sports.application.athlete.dto.AthleteRequest;
import gov.idrd.sports.application.athlete.dto.AthleteResponse;

import java.util.List;

public interface AthleteUseCase {

    AthleteResponse createAthlete(AthleteRequest request);

    AthleteResponse getAthleteById(Long id);

    List<AthleteResponse> getAllAthletes();

    AthleteResponse updateAthlete(Long id, AthleteRequest request);

    void deleteAthlete(Long id);
}
