package pl.wsb.fitnesstracker.training.internal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainingReportScheduler {

    @Autowired
    private TrainingReportService trainingReportService;

    // Every week on Sunday 23:59
    @Scheduled(cron = "0 59 23 ? * SUN")
    public void generateWeeklyReport() {
        trainingReportService.generateWeeklyReport();
    }
}
