package com.starlightonlinestore.data.repository;

import com.starlightonlinestore.data.models.OTPToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

@Transactional
public interface OtpTokenRepository extends JpaRepository<OTPToken, Integer> {

    Optional<OTPToken> findByToken(String token);

    void deleteOtpTokensByExpiredAtBefore(LocalDateTime currentTime);

    @Modifying
    @Query("UPDATE OTPToken otpToken " +
            "SET otpToken.verifiedAt= ?1 " +
            "WHERE otpToken.token = ?2")
    void setVerifiedAt(LocalDateTime verified, String token);
}
