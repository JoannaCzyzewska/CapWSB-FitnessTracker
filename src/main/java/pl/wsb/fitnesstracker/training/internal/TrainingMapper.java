package pl.wsb.fitnesstracker.training.internal;

import org.springframework.stereotype.Component;
import pl.wsb.fitnesstracker.training.api.Training;

@Component
class TrainingMapper {
    public TrainingDto toDto(Training training) {
        return new TrainingDto(
                training.getUser(),
                training.getStartTime(),
                training.getEndTime(),
                training.getDistance(),
                training.getAverageSpeed()
        );
    }
}
