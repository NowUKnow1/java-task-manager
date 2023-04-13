package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 1)
    private String firstName;

    @NotBlank
    @Size(min = 1)
    private String lastName;

    @NotBlank
    @Size(min = 3)
    private String password;
}
