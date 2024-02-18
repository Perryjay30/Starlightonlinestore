package com.starlightonlinestore.config.oauth2.successhandlers;

import com.example.oauth2sociallogin.exceptions.OAuth2SocialLoginException;
import com.example.oauth2sociallogin.security.oauth2.CustomOauth2User;
import com.example.oauth2sociallogin.user.data.model.AuthProvider;
import com.example.oauth2sociallogin.user.data.model.User;
import com.example.oauth2sociallogin.user.repository.UserRepository;
import com.starlightonlinestore.config.oauth2.CustomOauth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class GoogleOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;

    public GoogleOAuth2LoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOauth2User oauth2User = (CustomOauth2User)authentication.getPrincipal();
        String emailAddress = oauth2User.getEmail();
        String firstName = oauth2User.getName();
        Optional<User> user = userRepository.findByEmailAddress(emailAddress);
        if (user.isEmpty()) {
            createNewUserAfterOAuthLoginSuccess(emailAddress, firstName);
        } else {
            updateUserAfterOAuthLoginSuccess(emailAddress, firstName);
        }

        System.out.println("User's emailAddress: " + emailAddress);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void updateUserAfterOAuthLoginSuccess(String emailAddress, String firstName) {
        User existingUser = userRepository.findByEmailAddress(emailAddress).orElseThrow(() ->
            new OAuth2SocialLoginException("User not found!!"));
        existingUser.setAuthProvider(AuthProvider.GOOGLE);
        existingUser.setFirstName(firstName);
        userRepository.save(existingUser);
    }

    private void createNewUserAfterOAuthLoginSuccess(String emailAddress, String firstName) {
        User user = new User();
        user.setEmailAddress(emailAddress);
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.setFirstName(firstName);
        userRepository.save(user);
    }
}
