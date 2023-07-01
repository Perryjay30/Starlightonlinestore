package com.starlightonlinestore.data.dto.Request;

import com.starlightonlinestore.utils.validators.UserDetailsValidator;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.mindrot.jbcrypt.BCrypt;

@Data
public class ChangePasswordRequest {
//    @NotBlank(message = "This field must not be empty")
//    private String email;
    @NotBlank(message = "This field must not be empty")
    private String oldPassword;
    @NotBlank(message = "This field must not be empty")
    private String newPassword;


    public String getNewPassword() {
        if(UserDetailsValidator.isValidPassword(newPassword))
            return BCrypt.hashpw(newPassword, BCrypt.gensalt());
        else
            throw new RuntimeException("password must contain at least one " +
                    "capital letter, small letter and special characters");
    }
}
