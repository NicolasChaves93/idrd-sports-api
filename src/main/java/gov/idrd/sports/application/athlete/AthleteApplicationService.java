package gov.idrd.sports.application.athlete;

import gov.idrd.sports.application.athlete.dto.AthleteRequest;
import gov.idrd.sports.application.athlete.dto.AthleteResponse;
import gov.idrd.sports.domain.athlete.Athlete;
import gov.idrd.sports.domain.athlete.port.AthleteRepositoryPort;
import gov.idrd.sports.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AthleteApplicationService implements AthleteUseCase {

    private final AthleteRepositoryPort athleteRepositoryPort;

    public AthleteApplicationService(AthleteRepositoryPort athleteRepositoryPort) {
        this.athleteRepositoryPort = athleteRepositoryPort;
    }

    @Override
    @Transactional
    public AthleteResponse createAthlete(AthleteRequest request) {
        Athlete athlete = Athlete.create(
                request.name(),
                request.age(),
                request.category(),
                request.trainerId());

        Athlete savedAthlete = athleteRepositoryPort.save(athlete);
        return toResponse(savedAthlete);
    }

    @Override
    public AthleteResponse getAthleteById(Long id) {
        Athlete athlete = findAthleteOrThrow(id);
        return toResponse(athlete);
    }

    @Override
    public List<AthleteResponse> getAllAthletes() {
        return athleteRepositoryPort.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public AthleteResponse updateAthlete(Long id, AthleteRequest request) {
        Athlete existingAthlete = findAthleteOrThrow(id);
        existingAthlete.updateProfile(
                request.name(),
                request.age(),
                request.category(),
                request.trainerId());

        Athlete savedAthlete = athleteRepositoryPort.save(existingAthlete);
        return toResponse(savedAthlete);
    }

    @Override
    @Transactional
    public void deleteAthlete(Long id) {
        if (!athleteRepositoryPort.existsById(id)) {
            throw new ResourceNotFoundException("Athlete with id " + id + " was not found");
        }
        athleteRepositoryPort.deleteById(id);
    }

    private Athlete findAthleteOrThrow(Long id) {
        return athleteRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Athlete with id " + id + " was not found"));
    }

    private AthleteResponse toResponse(Athlete athlete) {
        return new AthleteResponse(
                athlete.getId(),
                athlete.getName(),
                athlete.getAge(),
                athlete.getCategory(),
                athlete.getTrainerId());
    }
}
