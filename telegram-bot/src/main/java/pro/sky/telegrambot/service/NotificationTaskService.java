package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTaskModel;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.management.Notification;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificationTaskService {

    private final NotificationTaskRepository notificationTaskRepository;
    private final TelegramBot telegramBot;

    public NotificationTaskService(NotificationTaskRepository notificationTaskRepository, TelegramBot telegramBot) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramBot = telegramBot;
    }

    @Scheduled(cron = "0 * * * * *")
    public void sendNotification(){
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        List<NotificationTaskModel> tasks = notificationTaskRepository.findByNotificationDateTimeLessThanEqual(now);

        for(NotificationTaskModel task : tasks){
            SendMessage request = new SendMessage(task.getChatId(),task.getNotificationText());

            SendResponse response = telegramBot.execute(request);

            if(response.isOk()){
                notificationTaskRepository.delete(task);
            }else {
                System.err.println("Не удалось отправить сообщение для задачи ID: "+task.getId()+ ". Ошибка: "+ response.errorCode());

            }
        }

    }
}
