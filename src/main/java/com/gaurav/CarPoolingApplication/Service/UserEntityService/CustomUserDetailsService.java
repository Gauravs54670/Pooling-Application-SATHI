package com.gaurav.CarPoolingApplication.Service.UserEntityService;

import com.gaurav.CarPoolingApplication.Entity.UserEntityPackage.UserEntity;
import com.gaurav.CarPoolingApplication.Exception.UserNotFoundException;
import com.gaurav.CarPoolingApplication.Repository.UserEntityRepository;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;
    public CustomUserDetailsService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }
    @Override @NonNull
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = this.userEntityRepository.findByEmailOrPhoneNumber(username,username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        Set<GrantedAuthority> grantedAuthorities = user.getUserRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
        return User.builder()
                .authorities(grantedAuthorities)
                .username(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
