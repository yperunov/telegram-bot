package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.model.NotificationTask;

import java.util.Collection;

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask,Long> {

    @Query("FROM NotificationTask WHERE timeStamp <= current_date")
    Collection<NotificationTask> getScheduledTasks();

}
