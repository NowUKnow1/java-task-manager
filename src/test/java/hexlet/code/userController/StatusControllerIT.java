package hexlet.code.userController;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.model.Status;
import hexlet.code.model.User;
import hexlet.code.repository.StatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hexlet.code.config.SpringConfigForIT.TEST_PROFILE;
import static hexlet.code.utils.TestUtils.ID;
import static hexlet.code.utils.TestUtils.BASE_STATUS_URL;
import static hexlet.code.utils.TestUtils.TEST_USERNAME;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.fromJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
public class StatusControllerIT {
    @Autowired
    private StatusRepository taskStatusRepository;
    @Autowired
    private TestUtils utils;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void clear() {
        utils.clearDB();
    }

    @Test
    public void testCreatedLabel() throws Exception {
        utils.regDefaultUser();
        utils.createNewTaskStatus().andExpect(status().isCreated());
        assertEquals(1, taskStatusRepository.count());
    }

    @Test
    public void testGetAll() throws Exception {
        utils.regDefaultUser();
        User user = userRepository.findAll().get(0);
        utils.createNewTaskStatus();
        final var response = utils.perform(get(BASE_STATUS_URL), user.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Label> labels = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(labels).hasSize(1);
    }

    @Test
    public void testGetLabelById() throws Exception {
        utils.regDefaultUser();
        User user = userRepository.findAll().get(0);
        utils.createNewTaskStatus();
        Status expectedStatus = taskStatusRepository.findAll().get(0);
        final var responseGet = utils.perform(
                        get(BASE_STATUS_URL + ID, expectedStatus.getId()),
                        user.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Status taskStatus = fromJson(responseGet.getContentAsString(), new TypeReference<Status>() {
        });

        assertEquals(expectedStatus.getCreatedAt().getTime(), taskStatus.getCreatedAt().getTime());
        assertEquals(expectedStatus.getName(), taskStatus.getName());
    }
    @Test
    public void testUpdateLabel() throws Exception {
        utils.regDefaultUser();
        User user = userRepository.findAll().get(0);
        utils.createNewTaskStatus();
        Status expectedStatus = taskStatusRepository.findAll().get(0);
        LabelDto newLabelDto = new LabelDto("new new status");
        final var responsePut = utils.perform(
                        put(BASE_STATUS_URL + ID, expectedStatus.getId())
                                .content(asJson(newLabelDto))
                                .contentType(APPLICATION_JSON), user.getEmail())
                .andReturn().getResponse();

        Label expected = fromJson(responsePut.getContentAsString(), new TypeReference<Label>() {
        });

        assertTrue(taskStatusRepository.existsById(expected.getId()));
        assertThat(expected.getName()).isEqualTo("new new status");
    }

    @Test
    public void deleteLabelById() throws Exception {
        utils.regDefaultUser();
        assertThat(taskStatusRepository.count()).isEqualTo(0);
        utils.createNewTaskStatus();
        assertThat(taskStatusRepository.count()).isEqualTo(1);
        Status taskStatus = taskStatusRepository.findAll().get(0);
        utils.perform(delete(BASE_STATUS_URL + ID, taskStatus.getId()), TEST_USERNAME)
                .andExpect(status().isOk());
        assertThat(taskStatusRepository.count()).isEqualTo(0);
    }


}
