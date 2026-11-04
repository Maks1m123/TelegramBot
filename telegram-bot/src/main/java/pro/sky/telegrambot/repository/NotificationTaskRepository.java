package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.NotificationTaskModel;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTaskModel, Long> {

    List<NotificationTaskModel> findByNotificationDateTimeLessThanEqual(LocalDateTime dateTime);
}