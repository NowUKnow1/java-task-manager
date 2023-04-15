package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;

    @Override
    public Label getLabelById(long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Label with that id is not exist"));
    }

    @Override
    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    @Override
    public Label createNewLabel(LabelDto labelDTO) {
        Label label = new Label();
        label.setName(labelDTO.getName());
        return labelRepository.save(label);
    }

    @Override
    public Label updateLabel(long id, LabelDto labelDTO) {
        Label label = getLabelById(id);
        label.setName(labelDTO.getName());
        return labelRepository.save(label);
    }

    @Override
    public void deleteLabelById(long id) {
        labelRepository.deleteById(id);
    }
}
