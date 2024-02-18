package com.starlightonlinestore.config.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOauth2User implements OAuth2User {

    private final String clientName;
    private final OAuth2User oAuth2User;

    public CustomOauth2User(String clientName, OAuth2User oAuth2User) {
        this.clientName = clientName;
        this.oAuth2User = oAuth2User;
    }

    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }
    public String getLogin() {
        return oAuth2User.getAttribute("login");
    }
    public String getClientName() {
        return this.clientName;
    }
}
