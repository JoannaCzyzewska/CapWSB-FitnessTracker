package pl.wsb.fitnesstracker.training.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> findAllByUserId(Long userId);

    @Query("SELECT t FROM Training t WHERE t.user.id = :userId " +
            "AND t.startTime >= :startDate AND t.endTime <= :endDate")
    List<Training> findTrainingsByUserAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT COUNT(t) FROM Training t WHERE t.user.id = :userId")
    Long countTrainingsByUser(@Param("userId") Long userId);
}
