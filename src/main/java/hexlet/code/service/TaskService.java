package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;

public interface TaskService {
    Task createNewTask(TaskDto dto);
    Task updateTask(long id, TaskDto dto);
    Task getTaskById(long id);
    void deleteTaskById(long id);
    Iterable<Task> getAllTasks(Predicate predicate);
}
