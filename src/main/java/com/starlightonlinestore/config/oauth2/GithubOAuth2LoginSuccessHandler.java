package com.starlightonlinestore.config.oauth2;

import com.example.oauth2sociallogin.exceptions.OAuth2SocialLoginException;
import com.example.oauth2sociallogin.security.oauth2.CustomOauth2User;
import com.example.oauth2sociallogin.user.data.model.AuthProvider;
import com.example.oauth2sociallogin.user.data.model.User;
import com.example.oauth2sociallogin.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class GithubOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;

    public GithubOAuth2LoginSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOauth2User oauth2User = (CustomOauth2User)authentication.getPrincipal();
        String username = oauth2User.getLogin();
        String firstName = oauth2User.getName();
        Optional<User> user = userRepository.findByEmailAddress(username);
        if (user.isEmpty()) {
            createNewUserAfterGithubOAuthLoginSuccess(username, firstName);
        } else {
            updateUserAfterGithubOAuthLoginSuccess(username, firstName);
        }

        System.out.println("User's username: " + username);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void updateUserAfterGithubOAuthLoginSuccess(String emailAddress, String firstName) {
        User existingUser = userRepository.findByEmailAddress(emailAddress).orElseThrow(() ->
                new OAuth2SocialLoginException("User not found!!"));
        existingUser.setAuthProvider(AuthProvider.GITHUB);
        existingUser.setFirstName(firstName);
        userRepository.save(existingUser);
    }

    private void createNewUserAfterGithubOAuthLoginSuccess(String emailAddress, String firstName) {
        User user = new User();
        user.setEmailAddress(emailAddress);
        user.setAuthProvider(AuthProvider.GITHUB);
        user.setFirstName(firstName);
        userRepository.save(user);
    }
}
