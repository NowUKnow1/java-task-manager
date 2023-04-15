package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;

import java.util.List;

public interface UserService {

    User createNewUser(UserDto userDto);

    void delete(Long id);

    User updateUser(long id, UserDto dto);

    String getCurrentUserName();

    User getCurrentUser();

    User getUserById(long id);
    List<User> getAllUsers();
}
