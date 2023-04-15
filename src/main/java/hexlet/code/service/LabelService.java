package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;

import java.util.List;

public interface LabelService {
    Label createNewLabel(LabelDto dto);

    Label updateLabel(long id, LabelDto dto);

    Label getLabelById(long id);
    List<Label> getAllLabels();
    void deleteLabelById(long id);
}
