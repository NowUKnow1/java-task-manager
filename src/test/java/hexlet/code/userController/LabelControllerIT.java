package hexlet.code.userController;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForIT;
import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
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
import static hexlet.code.utils.TestUtils.BASE_LABEL_URL;
import static hexlet.code.utils.TestUtils.ID;
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
public class LabelControllerIT {
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
    public void testCreatedLabel() throws Exception {
        utils.regDefaultUser();
        utils.createNewLabel().andExpect(status().isCreated());
        assertEquals(1, labelRepository.count());
    }

    @Test
    public void testGetAll() throws Exception {
        utils.regDefaultUser();
        utils.createNewLabel();
        User user = userRepository.findAll().get(0);
        final var response = utils.perform(get(BASE_LABEL_URL), user.getEmail())
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
        utils.createNewLabel();
        Label expectedLabel = labelRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);

        final var response = utils.perform(get(BASE_LABEL_URL + ID, expectedLabel.getId()), user.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        Label label = fromJson(response.getContentAsString(), new TypeReference<Label>() {
        });

        assertEquals(expectedLabel.getName(), label.getName());
        assertEquals(expectedLabel.getCreatedAt().getTime(), label.getCreatedAt().getTime());
    }

    @Test
    public void testUpdateLabel() throws Exception {
        utils.regDefaultUser();
        utils.createNewLabel();
        User user = userRepository.findAll().get(0);
        Label expectedLabel = labelRepository.findAll().get(0);
        LabelDto newLabelDto = new LabelDto("new new label");
        final var responsePut = utils.perform(put(BASE_LABEL_URL + ID, expectedLabel.getId())
                        .content(asJson(newLabelDto))
                        .contentType(APPLICATION_JSON), user.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Label label = fromJson(responsePut.getContentAsString(), new TypeReference<Label>() {
        });

        assertTrue(labelRepository.existsById(label.getId()));
        assertEquals(expectedLabel.getId(), label.getId());
        assertThat(label.getName()).isEqualTo("new new label");
    }

    @Test
    public void deleteLabelById() throws Exception {
        utils.regDefaultUser();
        assertThat(labelRepository.count()).isEqualTo(0);
        utils.createNewLabel();
        assertThat(labelRepository.count()).isEqualTo(1);
        User user = userRepository.findAll().get(0);
        Label label = labelRepository.findAll().get(0);
        utils.perform(delete(BASE_LABEL_URL + ID, label.getId()), user.getEmail())
                .andExpect(status().isOk());
        assertThat(labelRepository.count()).isEqualTo(0);
    }

}
