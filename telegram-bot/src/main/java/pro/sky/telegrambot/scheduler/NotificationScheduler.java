    package pro.sky.telegrambot.scheduler;

    import com.pengrad.telegrambot.TelegramBot;
    import com.pengrad.telegrambot.request.SendMessage;
    import org.springframework.scheduling.annotation.Scheduled;
    import org.springframework.stereotype.Component;
    import pro.sky.telegrambot.model.NotificationTaskModel;
    import pro.sky.telegrambot.repository.NotificationTaskRepository;

    import java.time.LocalDateTime;
    import java.time.temporal.ChronoUnit;
    import java.util.List;

    @Component
    public class NotificationScheduler {
        private final NotificationTaskRepository notificationTaskRepository;
        private final TelegramBot telegramBot;

        public NotificationScheduler(NotificationTaskRepository notificationTaskRepository, TelegramBot telegramBot) {
            this.notificationTaskRepository = notificationTaskRepository;
            this.telegramBot = telegramBot;
        }
        @Scheduled(cron = "0 * * * * *")
        public void sendNotification() {
            LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

            List<NotificationTaskModel> tasks = notificationTaskRepository.findByNotificationDateTimeLessThanEqual(now);
            for (NotificationTaskModel task : tasks) {
                telegramBot.execute(new SendMessage(task.getChatId(),"Напоминание: "+ task.getNotificationText()));

                notificationTaskRepository.delete(task);
            }
        }
    }
