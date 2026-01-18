package pl.wsb.fitnesstracker.training.internal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wsb.fitnesstracker.mail.api.EmailDto;
import pl.wsb.fitnesstracker.mail.api.EmailSender;
import pl.wsb.fitnesstracker.mail.internal.JavaMailEmailSender;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingRepository;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.internal.UserServiceImpl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class TrainingReportService {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private EmailSender emailSender;

    public void generateWeeklyReport() {
        log.info("Generowanie tygodniowego raportu...");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekStart = now.minusDays(7).with(LocalTime.MIN);
        LocalDateTime weekEnd = now.with(LocalTime.MAX);

        List<User> users = userService.findAllUsers();
        int emailsSent = 0;

        for (User user : users) {
            try {
                List<Training> weeklyTrainings = trainingRepository.findTrainingsByUserAndDateRange(
                        user.getId(), weekStart, weekEnd
                );

                Long totalTrainings = trainingRepository.countTrainingsByUser(user.getId());

                generateConsoleReport(user, weeklyTrainings, totalTrainings, weekStart, weekEnd);

                if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                    EmailDto email = generateEmailDto(user, weeklyTrainings, totalTrainings, weekStart, weekEnd);
                    emailSender.send(email);
                    emailsSent++;
                }

            } catch (Exception e) {
                log.error("Błąd podczas przetwarzania użytkownika {}: {}",
                        user.getEmail(), e.getMessage(), e);
            }
        }

        log.info("Generowanie raportu zakończone.");
    }

    private EmailDto generateEmailDto(User user,
                                      List<Training> weeklyTrainings,
                                      Long totalTrainings,
                                      LocalDateTime weekStart,
                                      LocalDateTime weekEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String subject = String.format("Raport treningowy - tydzień %s",
                weekStart.format(dateFormatter));

        StringBuilder content = new StringBuilder();

        content.append("=== RAPORT TYGODNIOWY ===\n\n");
        content.append("Użytkownik: ").append(user.getFirstName())
                .append(" ").append(user.getLastName()).append("\n");
        content.append("Okres: ").append(weekStart.format(formatter))
                .append(" - ").append(weekEnd.format(formatter)).append("\n");
        content.append("Liczba treningów w tym tygodniu: ").append(weeklyTrainings.size()).append("\n");
        content.append("Łączna liczba wszystkich treningów: ").append(totalTrainings).append("\n\n");

        if (!weeklyTrainings.isEmpty()) {
            content.append("--- Szczegóły treningów ---\n");
            weeklyTrainings.forEach(training -> {
                content.append("\n- ID: ").append(training.getId());
                content.append("\n  Data rozpoczęcia: ").append(training.getStartTime());
                content.append("\n  Data zakończenia: ").append(training.getEndTime());
                content.append("\n  Typ aktywności: ").append(training.getActivityType());
                content.append("\n  Dystans: ").append(training.getDistance()).append(" km");
                content.append("\n  Średnia prędkość: ").append(training.getAverageSpeed()).append(" km/h");
                content.append("\n");
            });
        } else {
            content.append("Brak treningów w tym tygodniu.\n");
        }

        content.append("\n==========================\n");

        String defaultSenderEmail = "test@example.com";
        return new EmailDto(
                user.getEmail(),
                defaultSenderEmail,
                subject,
                content.toString()
        );

    }

    private void generateConsoleReport(User user, List<Training> weeklyTrainings,
                                       Long totalTrainings, LocalDateTime weekStart,
                                       LocalDateTime weekEnd) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        System.out.println("\n=== RAPORT TYGODNIOWY ===");
        System.out.println("Użytkownik: " + user.getFirstName() + " " + user.getLastName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Okres: " + weekStart.format(formatter) + " - " + weekEnd.format(formatter));
        System.out.println("Liczba treningów w tym tygodniu: " + weeklyTrainings.size());
        System.out.println("Łączna liczba wszystkich treningów: " + totalTrainings);
        System.out.println("--- Szczegóły treningów ---");

        if (weeklyTrainings.isEmpty()) {
            System.out.println("Brak treningów w tym tygodniu.");
        } else {
            weeklyTrainings.forEach(training -> {
                System.out.println(
                        " - ID: " + training.getId() +
                        " | Data rozpoczęcia: " + training.getStartTime() +
                        " | Data zakończenia: " + training.getEndTime() +
                        " | Typ aktywności: " + training.getActivityType() +
                        " | Dystans: " + training.getDistance() +
                        " | Średnia prędkość: " + training.getAverageSpeed());
            });
        }

        System.out.println("==========================\n");
    }

}
