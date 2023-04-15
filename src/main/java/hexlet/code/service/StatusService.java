package hexlet.code.service;

import hexlet.code.dto.StatusDto;
import hexlet.code.model.Status;

import java.util.List;

public interface StatusService {

    Status createNewStatus(StatusDto statusDto);
    Status updateStatus(long id, StatusDto dto);
    List<Status> getAllTaskStatuses();
    Status getTaskStatusById(long id);
    void deleteTaskStatusById(long id);

}
