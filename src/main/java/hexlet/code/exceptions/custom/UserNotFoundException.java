package hexlet.code.exceptions.custom;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
