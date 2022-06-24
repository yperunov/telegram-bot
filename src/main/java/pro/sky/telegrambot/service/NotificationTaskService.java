package pro.sky.telegrambot.service;

import pro.sky.telegrambot.model.NotificationTask;

import java.util.Optional;
import java.util.function.Consumer;

public interface NotificationTaskService {
    public NotificationTask scheduleTask(Long chatId, NotificationTask task);
    public Optional<NotificationTask> parseToNotificationTask(String unformattedTask);

    void notifyAllScheduledTasks(Consumer<NotificationTask> notifier);

}
