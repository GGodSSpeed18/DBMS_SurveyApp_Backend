package com.example.survey.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.survey.data_transfer_objects.ActionLog;
import com.example.survey.data_transfer_objects.LoginResponse;
import com.example.survey.data_transfer_objects.LoginUser;
import com.example.survey.data_transfer_objects.RegisterUser;
import com.example.survey.data_transfer_objects.UserDTO;
import com.example.survey.entities.User;
import com.example.survey.services.AuthenticationService;
import com.example.survey.services.JwtService;
import com.example.survey.services.LogService;
import com.example.survey.utilities.TokenBlacklist;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    @Autowired
    private LogService logService;

    @Autowired
    TokenBlacklist tokenBlacklist;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public UserDTO register(@RequestBody RegisterUser registerUserDto) {
        return new UserDTO(authenticationService.signup(registerUserDto));
    }

    @PostMapping("/login")
    public LoginResponse authenticate(@RequestBody LoginUser loginUserDto, HttpServletRequest request) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        ActionLog logEntry = new ActionLog(new Date(), authenticatedUser.getUser_id(), request.getHeader("User-Agent"),
                logService.getLocationFromRequest(request), true);
        logService.saveLog(logEntry);
        return loginResponse;
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader, HttpServletRequest request) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User authenticatedUser = (User) authentication.getPrincipal();
            String token = authHeader.substring(7);
            tokenBlacklist.blacklistToken(token);
            ActionLog logEntry = new ActionLog(new Date(), authenticatedUser.getUser_id(),
                    request.getHeader("User-Agent"),
                    logService.getLocationFromRequest(request), false);
            logService.saveLog(logEntry);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/check")
    public Boolean CheckToken(@RequestBody String token) {
        if (token == null)
            return false;
        return tokenBlacklist.isTokenBlacklisted(token);
    }

}
