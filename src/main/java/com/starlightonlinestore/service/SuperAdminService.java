package com.starlightonlinestore.service;

import com.starlightonlinestore.data.exceptions.StoreException;
import com.starlightonlinestore.data.models.AuthProvider;
import com.starlightonlinestore.data.models.User;
import com.starlightonlinestore.data.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import static com.starlightonlinestore.data.models.Role.SUPER_ADMIN;
import static com.starlightonlinestore.data.models.Status.VERIFIED;

@Service
@RequiredArgsConstructor
public class SuperAdminService {

    private final UserRepository userRepository;

//    @EventListener(ApplicationReadyEvent.class)
    @PostConstruct
    public void createSuperAdmin() {
        try {
            if (userRepository.findByRole(SUPER_ADMIN).isEmpty()) {
                User superAdmin = User.builder()
                        .email("pelumijsh@gmail.com")
                        .password(BCrypt.hashpw("KingPerry@29", BCrypt.gensalt()))
                        .lastName("Taiwo")
                        .authProvider(AuthProvider.LOCAL)
                        .firstName("Oluwapelumi").status(VERIFIED).role(SUPER_ADMIN).build();
                userRepository.save(superAdmin);
            }
        } catch (StoreException exception) {
            throw new StoreException(exception.getMessage());
        }
    }
}
