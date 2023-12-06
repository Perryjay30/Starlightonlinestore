package com.starlightonlinestore.config;

import com.starlightonlinestore.data.models.User;
import com.starlightonlinestore.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FetchUserDetailsFromDbService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       Optional<User> existingUser = userRepository.findByEmail(email);
       return existingUser.map(FetchUserDetailsFromDb::new).orElseThrow(() -> new RuntimeException("User not found"));
    }

//    Optional<Vendor> existingVendor = vendorRepository.findByEmail(email);
//       if(existingUser.isPresent())
//               return existingUser.map(FetchUserDetailsFromDb::new).orElseThrow(() -> new RuntimeException("User not found"));
//       else if (existingVendor.isPresent()) {
//        return existingVendor.map(FetchUserDetailsFromDb::new).orElseThrow(() -> new RuntimeException("User not found"));
//    }
}
