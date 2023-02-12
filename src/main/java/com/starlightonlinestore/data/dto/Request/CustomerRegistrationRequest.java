package com.starlightonlinestore.data.dto.Request;

import com.starlightonlinestore.utils.validators.UserDetailsValidator;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.mindrot.jbcrypt.BCrypt;

@Data
public class CustomerRegistrationRequest {
    @NotBlank(message = "This field is required")
    private String email;
    @NotBlank(message = "This field is required")
    private String password;
    @NotBlank(message = "This field is required")
    private String firstName;
    @NotBlank(message = "This field is required")
    private String lastName;

    public String getPassword() {
        if(UserDetailsValidator.isValidPassword(password))
            return BCrypt.hashpw(password, BCrypt.gensalt());
        else
            throw new RuntimeException("password must contain at least one " +
                    "capital letter, small letter and special characters");
    }


}
