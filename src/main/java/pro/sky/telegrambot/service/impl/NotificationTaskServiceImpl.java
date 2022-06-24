package pro.sky.telegrambot.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.NotificationTaskService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationTaskServiceImpl implements NotificationTaskService {

    Logger logger = LoggerFactory.getLogger(NotificationTaskService.class);

    private final NotificationTaskRepository repository;

    public NotificationTaskServiceImpl(NotificationTaskRepository repository) {
        this.repository = repository;
    }

    private static final String REGEX = "([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public NotificationTask scheduleTask(Long chatId, NotificationTask task) {
        task.setChatId(chatId);
        NotificationTask savingTask = repository.save(task);
        return savingTask;
    }
    @Override
    public Optional<NotificationTask> parseToNotificationTask(String unformattedTask) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(unformattedTask);
        NotificationTask result = null;
        try {
            if (matcher.matches()) {
                LocalDateTime timeStampParsed = LocalDateTime.parse(matcher.group(1),DATE_TIME_FORMATTER);
                String messageContentParsed = matcher.group(3);
                result = new NotificationTask(messageContentParsed,timeStampParsed);
            }
        } catch (Exception e) {
            logger.error("Problem with parsing. Can't create correct task", e);
        }
        return Optional.ofNullable(result);
    }

    public void notifyAllScheduledTasks(Consumer<NotificationTask> notifier) {
        Collection<NotificationTask> tasks = repository.getScheduledTasks();
        for (NotificationTask task:
             tasks) {
            notifier.accept(task);
            task.markAsSent();
        }
        repository.saveAll(tasks);
    }


}
