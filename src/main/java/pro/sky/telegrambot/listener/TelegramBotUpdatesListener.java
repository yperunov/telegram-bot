package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private static String STR_CMD = "/start";
    private static String GREET_MSG = "Welcome to our super-mega-cool chatbot!";
    private static String INVALID_CMD = "Invalid command. Please try again";
    private final TelegramBot telegramBot;
    private final NotificationTaskService notificationTaskService;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationTaskService notificationTaskService) {
        this.telegramBot = telegramBot;
        this.notificationTaskService = notificationTaskService;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void notifyScheduledTasks() {
        notificationTaskService.notifyAllScheduledTasks(this::sendMessage);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Message message = update.message();
            if (message.text().equals(STR_CMD)) {
                logger.info("Command's been recieved: " + STR_CMD);
                sendMessage(showChatId(message), GREET_MSG);
            } else {
                notificationTaskService
                        .parseToNotificationTask(message.text()).ifPresentOrElse(
                                task -> scheduleNotification(showChatId(message), task),
                                () -> sendMessage(showChatId(message), INVALID_CMD)
                        );
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void scheduleNotification(Long chatId, NotificationTask task) {
        notificationTaskService.scheduleTask(chatId, task);
        sendMessage(chatId,"Reminder with text: " + task.getMessageContent() + " was scheduled on " + task.getTimeStamp());
    }

    private void sendMessage(Long chatId, String messageContent) {
        SendMessage sendMessage = new SendMessage(chatId, messageContent);
        telegramBot.execute(sendMessage);
    }

    private void sendMessage(NotificationTask task) {
        sendMessage(task.getChatId(),task.getMessageContent());
    }

    private Long showChatId(Message message) {
        return message.chat().id();
    }

}
