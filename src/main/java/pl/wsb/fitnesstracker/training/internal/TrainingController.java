package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wsb.fitnesstracker.training.api.Training;

import java.util.List;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
class TrainingController {

    private final TrainingServiceImpl trainingService;
    private final TrainingMapper trainingMapper;

    /**
     * Retrieves a list of all trainings.
     *
     * @return a list of {@link TrainingDto} representing all trainings
     */
    @GetMapping
    public List<TrainingDto> getAllTrainings() {
        return trainingService.getAllTrainings()
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }

    /**
     * Retrieves a list of all trainings for dedicated user.
     *
     * @param userId
     * @return a list of {@link TrainingDto} representing all trainings for dedicated user
     */
    @GetMapping("/{userId}")
    public List<TrainingDto> getAllTrainingsForDedicatedUser(
            @PathVariable Long userId
    ) {
        return trainingService.getAllTrainingsForUser(userId)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
    }
}
