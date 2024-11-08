package com.example.survey.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.survey.data_access_layers.RoleRepository;
import com.example.survey.data_access_layers.UserRepository;
import com.example.survey.data_transfer_objects.LoginUser;
import com.example.survey.data_transfer_objects.RegisterUser;
import com.example.survey.entities.User;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @SuppressWarnings("unused")
    private final RoleRepository roleRepository;

    public AuthenticationService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User signup(RegisterUser input) {
        User user = new User(input);
        user.setPasskey(passwordEncoder.encode(input.getPassword()));
        // user.setEmail(input.getEmail());
        // user.setPasskey(passwordEncoder.encode(input.getPassword()));
        return userRepository.saveAndReturnUserFull(user);
    }

    public User authenticate(LoginUser input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()));

        return userRepository.findByEmail(input.getEmail());
    }
}
