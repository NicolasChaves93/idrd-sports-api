package gov.idrd.sports.infrastructure.adapters.in.web;

import gov.idrd.sports.application.athlete.AthleteUseCase;
import gov.idrd.sports.application.athlete.dto.AthleteRequest;
import gov.idrd.sports.application.athlete.dto.AthleteResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/athletes")
public class AthleteController {

    private final AthleteUseCase athleteUseCase;

    public AthleteController(AthleteUseCase athleteUseCase) {
        this.athleteUseCase = athleteUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AthleteResponse createAthlete(@Valid @RequestBody AthleteRequest request) {
        return athleteUseCase.createAthlete(request);
    }

    @GetMapping
    public List<AthleteResponse> getAthletes() {
        return athleteUseCase.getAllAthletes();
    }

    @GetMapping("/{id}")
    public AthleteResponse getAthleteById(@PathVariable Long id) {
        return athleteUseCase.getAthleteById(id);
    }

    @PutMapping("/{id}")
    public AthleteResponse updateAthlete(@PathVariable Long id, @Valid @RequestBody AthleteRequest request) {
        return athleteUseCase.updateAthlete(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAthlete(@PathVariable Long id) {
        athleteUseCase.deleteAthlete(id);
    }
}
