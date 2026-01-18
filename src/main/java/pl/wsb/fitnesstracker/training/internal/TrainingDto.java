package pl.wsb.fitnesstracker.training.internal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.wsb.fitnesstracker.user.api.User;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class TrainingDto {
    private User user;
    private Date startTime;
    private Date endTime;
    private double distance;
    private double averageSpeed;
}
