package hexlet.code.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.component.JWTHelper;
import hexlet.code.dto.LabelDto;
import hexlet.code.dto.StatusDto;
import hexlet.code.dto.TaskDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.Label;
import hexlet.code.model.Status;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.StatusRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
import static hexlet.code.controller.StatusController.STATUS_CONTROLLER_PATH;
import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class TestUtils {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StatusRepository taskStatusRepository;
    @Autowired
    private JWTHelper jwtHelper;

    public static final String ID = "/{id}";
    public static final String BASE_URL = "/api";
    public static final String BASE_USER_URL = BASE_URL + USER_CONTROLLER_PATH;
    public static final String BASE_LABEL_URL = BASE_URL + LABEL_CONTROLLER_PATH;
    public static final String BASE_STATUS_URL = BASE_URL + STATUS_CONTROLLER_PATH;
    public static final String BASE_TASK_URL = BASE_URL + TASK_CONTROLLER_PATH;

    public static final String TEST_USERNAME = "email@email.com";
    public static final String TEST_USERNAME_2 = "email2@email.com";
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    public void clearDB() {
        taskRepository.deleteAll();
        labelRepository.deleteAll();
        taskStatusRepository.deleteAll();
        userRepository.deleteAll();
    }

    private final UserDto testUserDto = new UserDto(
            "firstName",
            "lastName",
            TEST_USERNAME,
            "pass"
    );
    public UserDto getTestUserDto() {
        return testUserDto;
    }
    public User getUserByEmail(final String email) {
        return userRepository.findByEmail(email).get();
    }

    public ResultActions regDefaultUser() throws Exception {
        return regUser(testUserDto);
    }

    public ResultActions regUser(final UserDto dto) throws Exception {
        final var request = post(BASE_USER_URL)
                .content(asJson(dto))
                .contentType(APPLICATION_JSON);
        return perform(request);
    }


    public ResultActions createNewTaskStatus() throws Exception {
        StatusDto taskStatusDto = new StatusDto("new status");
        User user = userRepository.findAll().get(0);
        return perform(post(BASE_STATUS_URL)
                .content(asJson(taskStatusDto))
                .contentType(APPLICATION_JSON), user.getEmail());
    }
    public ResultActions createNewLabel() throws Exception {
        LabelDto labelDto = new LabelDto("new label");
        User user = userRepository.findAll().get(0);
        return perform(post(BASE_LABEL_URL)
                .content(asJson(labelDto))
                .contentType(APPLICATION_JSON), user.getEmail());
    }
    public ResultActions createNewTask() throws Exception {
        User user = userRepository.findAll().get(0);
        createNewLabel();
        Label label = labelRepository.findAll().get(0);
        createNewTaskStatus();
        Status taskStatus = taskStatusRepository.findAll().get(0);

        TaskDto taskDto = new TaskDto("Task",
                "description",
                user.getId(),
                taskStatus.getId(),
                Set.of(label.getId()));

        return perform(post(BASE_TASK_URL)
                .content(asJson(taskDto))
                .contentType(APPLICATION_JSON), user.getEmail());
    }


    public ResultActions perform(final MockHttpServletRequestBuilder request, final String byUser) throws Exception {
        final String token = jwtHelper.expiring(Map.of("username", byUser));
        request.header(AUTHORIZATION, token);

        return perform(request);
    }
    public ResultActions perform(final MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request);
    }


    public static String asJson(final Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }
    public static <T> T fromJson(final String json, final TypeReference<T> to) throws JsonProcessingException {
        return MAPPER.readValue(json, to);
    }
}
