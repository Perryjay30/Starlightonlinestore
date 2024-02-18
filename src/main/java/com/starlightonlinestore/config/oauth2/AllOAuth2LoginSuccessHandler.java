package com.starlightonlinestore.config.oauth2;

import com.starlightonlinestore.config.oauth2.successhandlers.FacebookOAuth2LoginSuccessHandler;
import com.starlightonlinestore.config.oauth2.successhandlers.GithubOAuth2LoginSuccessHandler;
import com.starlightonlinestore.config.oauth2.successhandlers.GoogleOAuth2LoginSuccessHandler;
import com.starlightonlinestore.data.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AllOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final GoogleOAuth2LoginSuccessHandler googleOAuth2LoginSuccessHandler;
    private final UserRepository userRepository;
    private final GithubOAuth2LoginSuccessHandler githubOAuth2LoginSuccessHandler;
    private final FacebookOAuth2LoginSuccessHandler facebookOAuth2LoginSuccessHandler;

    public AllOAuth2LoginSuccessHandler(GoogleOAuth2LoginSuccessHandler googleOAuth2LoginSuccessHandler,
                                        UserRepository userRepository, GithubOAuth2LoginSuccessHandler githubOAuth2LoginSuccessHandler, FacebookOAuth2LoginSuccessHandler facebookOAuth2LoginSuccessHandler) {
        this.googleOAuth2LoginSuccessHandler = googleOAuth2LoginSuccessHandler;
        this.userRepository = userRepository;
        this.githubOAuth2LoginSuccessHandler = githubOAuth2LoginSuccessHandler;
        this.facebookOAuth2LoginSuccessHandler = facebookOAuth2LoginSuccessHandler;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, ServletException {
        String provider = determineAuthProvider(authentication);
        System.out.println("The Authentication Provider is: " + provider);
        assert provider != null;
        if (provider.equals("Google")) {
            googleOAuth2LoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        } else if (provider.equals("GitHub")) {
            githubOAuth2LoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        } else if (provider.equals("Facebook")) {
            facebookOAuth2LoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        } else {
            System.out.println("The data is not being saved in the database");
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    private String determineAuthProvider(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomOauth2User oauth2User) {
            return oauth2User.getClientName();
        }
        return null;
    }
}

