package hexlet.code.userController;


import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.utils.TestUtils.BASE_TASK_URL;
import static hexlet.code.utils.TestUtils.ID;
import static hexlet.code.utils.TestUtils.TEST_USERNAME;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
public class TaskControllerIT {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TestUtils utils;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void clear() {
        utils.clearDB();
    }

    @Test
    public void testCreateNewTask() throws Exception {
        utils.regDefaultUser();
        assertThat(taskRepository.count()).isEqualTo(0);
        utils.createNewTask().andExpect(status().isCreated());
        assertThat(taskRepository.count()).isEqualTo(1);
    }

    @Test
    public void testGetById() throws Exception {
        utils.regDefaultUser();
        User user = userRepository.findAll().get(0);
        utils.createNewTask();
        Task expectedTask = taskRepository.findAll().get(0);
        final var responseGet = utils.perform(get(BASE_TASK_URL + ID,
                        expectedTask.getId()), user.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Task task = fromJson(responseGet.getContentAsString(), new TypeReference<Task>() {
        });
        assertEquals(expectedTask.getName(), task.getName());
        assertEquals(expectedTask.getDescription(), task.getDescription());
        assertEquals(expectedTask.getAuthor().getId(), task.getAuthor().getId());
        assertEquals(expectedTask.getCreatedAt().getTime(), task.getCreatedAt().getTime());
    }

    @Test
    public void testGetAllTask() throws Exception {
        utils.regDefaultUser();
        User user = userRepository.findAll().get(0);
        assertEquals(0, taskRepository.count());
        utils.createNewTask();
        final var responseGet = utils.perform(get(BASE_TASK_URL), user.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        List<Task> tasks = fromJson(responseGet.getContentAsString(), new TypeReference<List<Task>>() {
        });
        assertEquals(1, tasks.size());
    }

    @Test
    public void testUpdateTask() throws Exception {
        utils.regDefaultUser();
        User user = userRepository.findAll().get(0);
        utils.createNewTask();
        Task task = taskRepository.findAll().get(0);
        Label label = labelRepository.findAll().get(0);
        TaskDto taskDto = new TaskDto(
                "task",
                "des",
                task.getExecutor().getId(),
                task.getTaskStatus().getId(),
                Set.of(label.getId()));

        utils.perform(put(BASE_TASK_URL + ID, task.getId())
                        .content(asJson(taskDto))
                        .contentType(APPLICATION_JSON), user.getEmail())
                .andExpect(status().isOk());

        task = taskRepository.findAll().get(0);
        assertEquals(1, taskRepository.count());
        assertEquals("task", task.getName());
        assertEquals("des", task.getDescription());
    }

    @Test
    public void deleteById() throws Exception {
        utils.regDefaultUser();
        utils.createNewTask();
        Assertions.assertThat(taskRepository.count()).isEqualTo(1);
        Task task = taskRepository.findAll().get(0);
        utils.perform(delete(BASE_TASK_URL + ID, task.getId()), TEST_USERNAME)
                .andExpect(status().isOk());
        Assertions.assertThat(taskRepository.count()).isEqualTo(0);
    }


}
