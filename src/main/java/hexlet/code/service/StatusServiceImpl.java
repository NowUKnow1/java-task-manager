package hexlet.code.service;

import hexlet.code.dto.StatusDto;
import hexlet.code.model.Status;
import hexlet.code.repository.StatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {
    private final StatusRepository taskStatusRepository;

    @Override
    public List<Status> getAllTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    @Override
    public Status getTaskStatusById(long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task Status with that id is not exist"));
    }

    @Override
    public Status createNewStatus(StatusDto taskStatusDTO) {
        Status taskStatus = new Status();
        taskStatus.setName(taskStatusDTO.getName());
        return taskStatusRepository.save(taskStatus);
    }

    @Override
    public Status updateStatus(long id, StatusDto taskStatusDTO) {
        Status taskStatus = getTaskStatusById(id);
        taskStatus.setName(taskStatusDTO.getName());
        return taskStatusRepository.save(taskStatus);
    }

    @Override
    public void deleteTaskStatusById(long id) {
        taskStatusRepository.deleteById(id);
    }
}
