package com.starlightonlinestore.data.repository;

//import com.starlightonlinestore.data.models.Role;
import com.starlightonlinestore.data.models.Role;
import com.starlightonlinestore.data.models.User;
import com.starlightonlinestore.data.models.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("UPDATE User user SET user.status = ?1 WHERE user.email = ?2")
    void enableUser(Status verified, String email);

    Optional<User> findByRole(Role role);
}
