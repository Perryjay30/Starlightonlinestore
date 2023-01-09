package com.starlightonlinestore.data.dto.Request;

import com.starlightonlinestore.utils.validators.UserDetailsValidator;
import lombok.Data;
import org.mindrot.jbcrypt.BCrypt;

@Data
public class CustomerRegistrationRequest {
    private String email;
    private String password;
    private String phoneNumber;
    private String address;

    public String getPassword() {
        if(UserDetailsValidator.isValidPassword(password))
            return BCrypt.hashpw(password, BCrypt.gensalt());
        else
            throw new RuntimeException("password must contain at least one " +
                    "capital letter, small letter and special characters");
    }


}
